const estado = {
    carregando: false
};

const elementos = {
    nomeTorneio: document.querySelector("#nomeTorneio"),
    ranking: document.querySelector("#ranking"),
    rodadas: document.querySelector("#rodadas"),
    mensagem: document.querySelector("#mensagem"),
    formJogador: document.querySelector("#formJogador"),
    gerarRodada: document.querySelector("#gerarRodada"),
    reiniciar: document.querySelector("#reiniciar")
};

async function api(caminho, opcoes = {}) {
    const resposta = await fetch(caminho, {
        headers: { "Content-Type": "application/json" },
        ...opcoes
    });
    const dados = await resposta.json();
    if (!resposta.ok) {
        throw new Error(dados.erro || "Falha na operacao");
    }
    return dados;
}

async function carregar() {
    try {
        alternarCarregando(true);
        const torneio = await api("/api/torneio");
        renderizar(torneio);
        mostrarMensagem("");
    } catch (erro) {
        mostrarMensagem(erro.message);
    } finally {
        alternarCarregando(false);
    }
}

function renderizar(torneio) {
    elementos.nomeTorneio.textContent = torneio.nome;
    elementos.ranking.innerHTML = torneio.ranking.map((jogador, index) => `
        <article class="item">
            <span class="posicao">${index + 1}</span>
            <div>
                <div class="nome">${escapar(jogador.nome)}</div>
                <div class="meta">Rating ${jogador.rating} · Byes ${jogador.byes}</div>
            </div>
            <strong class="pontos">${jogador.pontuacao} pts</strong>
        </article>
    `).join("");

    elementos.rodadas.innerHTML = torneio.rodadas.length === 0
        ? `<p class="meta">Nenhuma rodada criada.</p>`
        : torneio.rodadas.slice().reverse().map(rodada => `
            <article class="rodada">
                <h3>Rodada ${rodada.numero}</h3>
                ${rodada.bye ? `<p class="bye">${escapar(rodada.bye.nome)} recebeu bye</p>` : ""}
                ${rodada.partidas.map(renderizarPartida).join("")}
            </article>
        `).join("");
}

function renderizarPartida(partida) {
    const resultado = partida.resultado
        ? `<span class="pontos">${traduzirResultado(partida.resultado)}</span>`
        : `
            <div class="resultado">
                <button data-partida="${partida.id}" data-resultado="VITORIA_BRANCAS">Brancas</button>
                <button data-partida="${partida.id}" data-resultado="EMPATE">Empate</button>
                <button data-partida="${partida.id}" data-resultado="VITORIA_PRETAS">Pretas</button>
            </div>
        `;

    return `
        <div class="partida">
            <div class="confronto">
                <span>♔ ${escapar(partida.brancas.nome)}</span>
                <span>♚ ${escapar(partida.pretas.nome)}</span>
            </div>
            ${resultado}
        </div>
    `;
}

function traduzirResultado(resultado) {
    const resultados = {
        VITORIA_BRANCAS: "Vitoria das brancas",
        VITORIA_PRETAS: "Vitoria das pretas",
        EMPATE: "Empate"
    };
    return resultados[resultado] || resultado;
}

function escapar(valor) {
    return String(valor)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;");
}

function mostrarMensagem(texto) {
    elementos.mensagem.textContent = texto;
}

function alternarCarregando(carregando) {
    estado.carregando = carregando;
    document.querySelectorAll("button").forEach(botao => {
        botao.disabled = carregando;
    });
}

elementos.formJogador.addEventListener("submit", async evento => {
    evento.preventDefault();
    const formulario = new FormData(evento.currentTarget);
    const payload = {
        nome: formulario.get("nome"),
        rating: Number(formulario.get("rating"))
    };

    try {
        alternarCarregando(true);
        await api("/api/jogadores", {
            method: "POST",
            body: JSON.stringify(payload)
        });
        evento.currentTarget.reset();
        document.querySelector("#rating").value = 1500;
        await carregar();
    } catch (erro) {
        mostrarMensagem(erro.message);
    } finally {
        alternarCarregando(false);
    }
});

elementos.gerarRodada.addEventListener("click", async () => {
    try {
        alternarCarregando(true);
        await api("/api/rodadas", { method: "POST", body: "{}" });
        await carregar();
    } catch (erro) {
        mostrarMensagem(erro.message);
    } finally {
        alternarCarregando(false);
    }
});

elementos.rodadas.addEventListener("click", async evento => {
    const botao = evento.target.closest("[data-partida]");
    if (!botao) {
        return;
    }

    try {
        alternarCarregando(true);
        await api(`/api/partidas/${botao.dataset.partida}/resultado`, {
            method: "POST",
            body: JSON.stringify({ resultado: botao.dataset.resultado })
        });
        await carregar();
    } catch (erro) {
        mostrarMensagem(erro.message);
    } finally {
        alternarCarregando(false);
    }
});

elementos.reiniciar.addEventListener("click", async () => {
    try {
        alternarCarregando(true);
        await api("/api/reiniciar", {
            method: "POST",
            body: JSON.stringify({ nome: "SystemChess Open" })
        });
        await carregar();
    } catch (erro) {
        mostrarMensagem(erro.message);
    } finally {
        alternarCarregando(false);
    }
});

carregar();
