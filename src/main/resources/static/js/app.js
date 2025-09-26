// ========= Helpers =========
const $  = sel => document.querySelector(sel);
const $$ = sel => Array.from(document.querySelectorAll(sel));

async function http(url, options = {}) {
  const r = await fetch(url, { credentials:'same-origin', ...options });
  const ct = (r.headers.get('content-type') || '').toLowerCase();
  if (!r.ok) {
    const txt = await r.text();
    throw new Error(txt || (r.status + ' ' + r.statusText));
  }
  return ct.includes('application/json') ? r.json() : r.text();
}

// Lê uma URL esperando **lista** JSON. Aceita:
//   - Array puro: [ ... ]
//   - Paginado Spring: { content: [ ... ], ... }
//   - Qualquer outra coisa => []
async function getList(url) {
  const r = await fetch(url, { credentials:'same-origin' });
  if (!r.ok) throw new Error(await r.text());

  let data;
  try {
    data = await r.json();
  } catch {
    return []; // não era JSON
  }
  if (Array.isArray(data)) return data;
  if (data && Array.isArray(data.content)) return data.content;
  return [];
}

function setMsg(id, text, ok = true) {
  const el = $(id);
  if (!el) return;
  el.textContent = text || '';
  el.style.color = ok ? '#79f2c0' : '#ff6b6b';
}

function isoToBr(d) {
  if (!d) return '';
  const s = d.split('T')[0];
  const [y,m,day] = s.split('-');
  return `${day}/${m}/${y}`;
}

// ========= Troca de painel =========
$$('.menu button').forEach(btn => {
  btn.addEventListener('click', async () => {
    $$('.menu button').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');

    $$('main section').forEach(s => s.hidden = true);
    const id = btn.dataset.panel;
    const sec = $(`#panel-${id}`);
    sec.hidden = false;

    try {
      if (id === 'usuarios') await carregarUsuarios();
      if (id === 'projetos') await carregarProjetos();
      if (id === 'equipes')  await carregarEquipes();
    } catch (e) {
      console.warn('Falha ao carregar painel:', e.message);
    }
  });
});

// ========= Listas =========
async function carregarUsuarios() {
  const arr = await getList('/api/usuarios');

  // select de gerente (apenas GERENTE ou ADMINISTRADOR)
  const selG = $('#sel-gerente');
  if (selG) {
    selG.innerHTML = '';
    arr
      .filter(u => u.perfil === 'GERENTE' || u.perfil === 'ADMINISTRADOR')
      .forEach(u => selG.insertAdjacentHTML(
        'beforeend',
        `<option value="${u.id}">${u.nomeCompleto} · ${u.perfil}</option>`
      ));
  }

  // select de membros
  const selM = $('#sel-membros');
  if (selM) {
    selM.innerHTML = '';
    arr.forEach(u => selM.insertAdjacentHTML(
      'beforeend',
      `<option value="${u.id}">${u.nomeCompleto} (${u.login})</option>`
    ));
  }

  // tabela de usuários (linhas clicáveis)
  const tb = $('#tbl-usuarios tbody');
  if (tb) {
    tb.innerHTML = '';
    arr.forEach(u => tb.insertAdjacentHTML(
      'beforeend',
      `<tr data-id="${u.id}" data-json='${JSON.stringify(u).replace(/'/g,"&#39;")}'>
         <td>${u.nomeCompleto || ''}</td>
         <td>${u.login || ''}</td>
         <td><span class="tag">${u.perfil || ''}</span></td>
         <td>${u.email || ''}</td>
       </tr>`
    ));
    tb.querySelectorAll('tr').forEach(tr => {
      tr.addEventListener('click', () => {
        const u = JSON.parse(tr.getAttribute('data-json').replace(/&#39;/g,"'"));
        preencherFormUsuario(u);
      });
    });
  }
}

async function carregarProjetos() {
  const arr = await getList('/api/projetos');

  const selP = $('#sel-projetos');
  if (selP) {
    selP.innerHTML = '';
    arr.forEach(p => selP.insertAdjacentHTML('beforeend', `<option value="${p.id}">${p.nome}</option>`));
  }

  const tb = $('#tbl-projetos tbody');
  if (tb) {
    tb.innerHTML = '';
    arr.forEach(p => tb.insertAdjacentHTML(
      'beforeend',
      `<tr>
         <td>${p.nome || ''}</td>
         <td><span class="tag">${p.status || ''}</span></td>
         <td>${(p.gerente && (p.gerente.nomeCompleto || p.gerente.login)) || '-'}</td>
         <td>${isoToBr(p.dataInicio)}</td>
       </tr>`
    ));
  }
}

async function carregarEquipes() {
  const arr = await getList('/api/equipes');
  const tb = $('#tbl-equipes tbody');
  if (tb) {
    tb.innerHTML = '';
    arr.forEach(e => tb.insertAdjacentHTML(
      'beforeend',
      `<tr>
         <td>${e.nome || ''}</td>
         <td>${(e.membros || []).map(m => m.nomeCompleto).join(', ')}</td>
       </tr>`
    ));
  }
}

// ========= Usuário: preencher / limpar / estado =========
function preencherFormUsuario(u){
  $('#usuario-id').value        = u.id ?? '';
  $('#usuario-nome').value      = u.nomeCompleto ?? '';
  $('#usuario-cpf').value       = u.cpf ?? '';
  $('#usuario-email').value     = u.email ?? '';
  $('#usuario-cargo').value     = u.cargo ?? '';
  $('#usuario-login').value     = u.login ?? '';
  $('#usuario-senha').value     = '';                 // nunca preenche senha
  $('#usuario-perfil').value    = u.perfil ?? 'COLABORADOR';

  $('#flag-edicao').hidden = false;
  $('#btn-salvar-usuario').textContent = 'Salvar alterações';
  $('#btn-cancelar-edicao').hidden = false;
}

function resetFormUsuario(){
  $('#form-usuario').reset();
  $('#usuario-id').value = '';
  $('#flag-edicao').hidden = true;
  $('#btn-salvar-usuario').textContent = 'Salvar usuário';
  $('#btn-cancelar-edicao').hidden = true;
  setMsg('#msg-usuario','');
}

$('#btn-cancelar-edicao')?.addEventListener('click', resetFormUsuario);

// ========= Submits =========
$('#form-usuario')?.addEventListener('submit', async (e) => {
  e.preventDefault();

  const fd = new FormData(e.target);
  const id = fd.get('id');
  const body = {
    nomeCompleto: fd.get('nomeCompleto'),
    cpf:          fd.get('cpf'),
    email:        fd.get('email'),
    cargo:        fd.get('cargo'),
    login:        fd.get('login'),
    perfil:       fd.get('perfil')
  };
  const senha = fd.get('senha');
  if (senha) body.senha = senha; // se vazio, backend mantém a atual

  try {
    if (id) {
      await http(`/api/usuarios/${id}`, {
        method:'PUT',
        headers:{'Content-Type':'application/json'},
        body: JSON.stringify(body)
      });
      setMsg('#msg-usuario','Usuário atualizado com sucesso.', true);
    } else {
      await http('/api/usuarios', {
        method:'POST',
        headers:{'Content-Type':'application/json'},
        body: JSON.stringify(body)
      });
      setMsg('#msg-usuario','Usuário salvo com sucesso.', true);
    }
    await carregarUsuarios();
    resetFormUsuario();
  } catch (err) {
    setMsg('#msg-usuario', 'Erro ao salvar usuário: ' + err.message, false);
  }
});

$('#form-projeto')?.addEventListener('submit', async (e) => {
  e.preventDefault();
  const fd = new FormData(e.target);
  const body = Object.fromEntries(fd.entries());
  body.gerenteId = body.gerenteId ? Number(body.gerenteId) : null;

  try {
    await http('/api/projetos', {
      method:'POST',
      headers:{'Content-Type':'application/json'},
      body: JSON.stringify(body)
    });
    setMsg('#msg-projeto','Projeto salvo com sucesso.', true);
    e.target.reset();
    await carregarProjetos();
  } catch (err) {
    setMsg('#msg-projeto','Erro ao salvar projeto: ' + err.message, false);
  }
});

$('#form-equipe')?.addEventListener('submit', async (e) => {
  e.preventDefault();

  const membros  = Array.from($('#sel-membros').selectedOptions || []).map(o => Number(o.value));
  const projetos = Array.from($('#sel-projetos')?.selectedOptions || []).map(o => Number(o.value));

  const fd = new FormData(e.target);
  const body = {
    nome: fd.get('nome'),
    descricao: fd.get('descricao'),
    membrosIds: membros,
    projetosIds: projetos
  };

  try {
    await http('/api/equipes', {
      method:'POST',
      headers:{'Content-Type':'application/json'},
      body: JSON.stringify(body)
    });
    setMsg('#msg-equipe','Equipe salva com sucesso.', true);
    e.target.reset();
    await carregarEquipes();
  } catch (err) {
    setMsg('#msg-equipe','Erro ao salvar equipe: ' + err.message, false);
  }
});

// ========= Boot =========
(async function init(){
  try{
    await carregarUsuarios();
    await carregarProjetos();
    await carregarEquipes();
  }catch(e){
    console.warn('Falha ao carregar dados iniciais:', e.message);
  }
})();
