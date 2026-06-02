# SystemChess

SystemChess e um sistema web para gerenciamento de torneios de xadrez. O projeto possui back end em Java 21, front end web estatico e organizacao inspirada em MVC, com regras de negocio separadas da interface e dos controladores HTTP.

## Estrutura

- `src/main/java/br/com/systemchess/model`: entidades de dominio do torneio.
- `src/main/java/br/com/systemchess/service`: regras de negocio e coordenacao do torneio.
- `src/main/java/br/com/systemchess/controller`: controladores da API.
- `src/main/java/br/com/systemchess/server`: servidor HTTP e roteamento.
- `src/main/java/br/com/systemchess/util`: utilitarios de serializacao.
- `src/main/resources/public`: front end responsivo com identidade visual de xadrez.
- `teste/java/br/com/systemchess`: testes automatizados autocontidos.
- `scripts`: comandos de build, teste e execucao.

## Funcionalidades

- Cadastro de jogadores com validacao de nome e rating.
- Bloqueio de nomes duplicados.
- Geracao de rodadas com pareamento por ranking.
- Controle de bye em torneios com numero impar de jogadores.
- Registro de vitoria das brancas, vitoria das pretas e empate.
- Bloqueio de resultado duplicado na mesma partida.
- Ranking por pontuacao, rating e nome.
- API HTTP para integracao com o front end.
- Interface responsiva com painel de ranking, rodadas e acoes do torneio.

## Como executar

No PowerShell, dentro da pasta do projeto:

```powershell
.\scripts\run.bat
```

A aplicacao ficara disponivel em:

```text
http://localhost:8080
```

Para usar outra porta:

```powershell
.\scripts\run.bat 9090
```

## Como testar

```powershell
.\scripts\test.bat
```

Os testes verificam regras de negocio importantes para a atividade de Teste e Qualidade de Software, incluindo validacoes, fluxo de rodada, pontuacao, ranking e tratamento de estados invalidos.

## API

- `GET /api/health`: verifica se o servidor esta ativo.
- `GET /api/torneio`: retorna jogadores, ranking e rodadas.
- `POST /api/jogadores`: cadastra jogador com `nome` e `rating`.
- `POST /api/rodadas`: gera nova rodada.
- `POST /api/partidas/{id}/resultado`: registra resultado da partida.
- `POST /api/reiniciar`: reinicia o torneio.

## Qualidade

O projeto aplica separacao de responsabilidades, encapsulamento, validacoes defensivas, testes automatizados e scripts reproduziveis. A camada de dominio nao depende da interface, a camada de servico concentra as regras da aplicacao e o front end consome a API de forma independente.
