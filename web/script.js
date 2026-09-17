const API = "http://localhost:8080";

// Guarda a última lista de salas vinda do backend.
// Serve para não precisar buscar tudo de novo quando o usuário clica em "Editar".
let salas = [];


// GET - Buscar e desenhar as salas na tela
async function carregarSalas() {

    const grid = document.getElementById("roomsGrid");

    try {

        const resposta = await fetch(API + "/salas");

        if (!resposta.ok) {
            throw new Error("Falha ao buscar salas");
        }

        salas = await resposta.json();

        renderizarSalas();

    } catch (error) {

        console.error("Não foi possível carregar as salas:", error);

        grid.innerHTML = `
            <p class="rooms-loading">
                Não foi possível conectar à API. Verifique se o servidor (ApiServer) está rodando na porta 8080.
            </p>
        `;
    }
}


// Transforma o valor salvo no banco (situacao) em texto/classe para o badge
function situacaoParaBadge(situacao) {

    if (situacao === "EM_MANUTENCAO") {
        return { texto: "manutenção", classe: "badge-alert" };
    }

    if (situacao === "INDISPONIVEL") {
        return { texto: "indisponível", classe: "badge-busy" };
    }

    // DISPONIVEL (ou qualquer outro valor inesperado) cai aqui
    return { texto: "livre", classe: "badge-free" };
}


// Desenha a lista de salas guardada em "salas" dentro do grid
function renderizarSalas() {

    const grid = document.getElementById("roomsGrid");

    if (salas.length === 0) {
        grid.innerHTML = `<p class="rooms-loading">Nenhuma sala cadastrada ainda.</p>`;
        return;
    }

    grid.innerHTML = "";

    salas.forEach(sala => {

        const badge = situacaoParaBadge(sala.situacao);

        const card = document.createElement("div");
        card.className = "card room-card";

        card.innerHTML = `
            <div class="room-info">
                <h3>${escapeHTML(sala.nomeSala)}</h3>
                <p>
                    Capacidade: ${escapeHTML(sala.capacidade)}
                    &middot;
                    ${escapeHTML(sala.recursos || "Sem recursos cadastrados")}
                </p>
                <div class="room-actions">
                    <button class="btn-edit" type="button">Editar</button>
                    <button class="btn-delete" type="button">Excluir</button>
                </div>
            </div>
            <span class="badge ${badge.classe}">${badge.texto}</span>
        `;

        // Em vez de usar onclick="..." no HTML, ligamos os eventos aqui,
        // já passando o id certinho da sala.
        card.querySelector(".btn-edit").addEventListener("click", () => editarSala(sala.id));
        card.querySelector(".btn-delete").addEventListener("click", () => excluirSala(sala.id));

        grid.appendChild(card);
    });
}


// DELETE - Excluir sala
async function excluirSala(id) {

    const confirmar = confirm("Deseja excluir esta sala?");

    if (!confirmar) {
        return;
    }

    try {

        const resposta = await fetch(API + "/salas/" + id, {
            method: "DELETE"
        });

        if (resposta.ok) {
            mostrarMensagem("Sala excluída com sucesso!", "sucesso");
            carregarSalas();
        } else {
            mostrarMensagem("Erro ao excluir sala.", "erro");
        }

    } catch (erro) {
        console.error(erro);
        mostrarMensagem("Erro ao conectar com a API.", "erro");
    }
}


// Preparar edição (só abre o modal já preenchido,
// quem realmente salva é o "submit" do formulário)
function editarSala(id) {

    const sala = salas.find(s => s.id === id);

    if (!sala) {
        mostrarMensagem("Sala não encontrada.", "erro");
        return;
    }

    abrirModal(sala);
}


// MODAL (criar / editar)
document.addEventListener("DOMContentLoaded", () => {

    const modal = document.getElementById("modalSala");
    const btnNovaSala = document.getElementById("btnNovaSala");
    const fecharModal = document.getElementById("fecharModal");
    const cancelarModal = document.getElementById("cancelarModal");
    const form = document.getElementById("formSala");

    const modalTitulo = document.getElementById("modalTitulo");
    const modalSubtitulo = document.getElementById("modalSubtitulo");

    /* ABRIR MODAL - botão "+ Nova sala" */
    btnNovaSala.addEventListener("click", () => {
        abrirModal(null);
    });

    /* FECHAR MODAL */
    fecharModal.addEventListener("click", fecharModalSala);
    cancelarModal.addEventListener("click", fecharModalSala);

    /* Fechar clicando fora do card do modal */
    modal.addEventListener("click", (event) => {
        if (event.target === modal) {
            fecharModalSala();
        }
    });

    /* Fechar com a tecla ESC */
    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape" && modal.classList.contains("active")) {
            fecharModalSala();
        }
    });

    /* CRIAR ou EDITAR (o mesmo formulário serve para os dois casos) */
    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const id = document.getElementById("salaId").value;
        const nome = document.getElementById("nomeSala").value.trim();
        const capacidade = document.getElementById("capacidadeSala").value;
        const recursos = document.getElementById("recursosSala").value.trim();
        const situacao = document.getElementById("statusSala").value;

        if (!nome || !capacidade) {
            mostrarMensagem("Preencha todos os campos obrigatórios.", "erro");
            return;
        }

        const sala = {
            situacao: situacao,
            nomeSala: nome,
            capacidade: Number(capacidade),
            recursos: recursos
        };

        const estaEditando = id !== "";

        try {

            const resposta = await fetch(
                estaEditando ? API + "/salas/" + id : API + "/salas",
                {
                    method: estaEditando ? "PATCH" : "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(sala)
                }
            );

            if (resposta.ok) {
                fecharModalSala();
                mostrarMensagem(
                    estaEditando ? "Sala atualizada com sucesso!" : `A sala "${nome}" foi cadastrada com sucesso!`,
                    "sucesso"
                );
                carregarSalas();
            } else {
                mostrarMensagem("Erro ao salvar a sala.", "erro");
            }

        } catch (erro) {
            console.error(erro);
            mostrarMensagem("Erro ao conectar com a API.", "erro");
        }
    });

    /* Abre o modal. Se "sala" for passado, entra em modo edição. */
    function abrirModal(sala) {

        form.reset();

        if (sala) {
            modalTitulo.textContent = "Editar sala";
            modalSubtitulo.textContent = "Altere os dados da sala";

            document.getElementById("salaId").value = sala.id;
            document.getElementById("nomeSala").value = sala.nomeSala;
            document.getElementById("capacidadeSala").value = sala.capacidade;
            document.getElementById("recursosSala").value = sala.recursos || "";
            document.getElementById("statusSala").value = sala.situacao;
        } else {
            modalTitulo.textContent = "Nova sala";
            modalSubtitulo.textContent = "Cadastre uma nova sala ou auditório";
            document.getElementById("salaId").value = "";
        }

        modal.classList.add("active");
        document.getElementById("nomeSala").focus();
    }

    function fecharModalSala() {
        modal.classList.remove("active");
        form.reset();
    }

    // Precisamos chamar abrirModal fora deste bloco (em editarSala), então a
    // deixamos acessível através do objeto window.
    window.abrirModal = abrirModal;
});


// MENSAGEM DE CONFIRMAÇÃO
function mostrarMensagem(texto, tipo) {

    const mensagemExistente = document.querySelector(".toast-mensagem");
    if (mensagemExistente) {
        mensagemExistente.remove();
    }

    const toast = document.createElement("div");
    toast.className = `toast-mensagem ${tipo}`;

    const icone = tipo === "sucesso" ? "✓" : "!";

    toast.innerHTML = `
        <div class="toast-icone">${icone}</div>
        <div class="toast-conteudo">
            <strong>${tipo === "sucesso" ? "Sucesso!" : "Atenção"}</strong>
            <span>${escapeHTML(texto)}</span>
        </div>
        <button class="toast-fechar">&times;</button>
    `;

    document.body.appendChild(toast);

    toast.querySelector(".toast-fechar").addEventListener("click", () => {
        toast.remove();
    });

    setTimeout(() => {
        if (toast.parentElement) {
            toast.remove();
        }
    }, 4000);
}


// SEGURANÇA - evita que texto digitado vire HTML/script
function escapeHTML(texto) {
    const div = document.createElement("div");
    div.textContent = texto;
    return div.innerHTML;
}


// Carrega as salas assim que a página abre
carregarSalas();
