# Onze

App Android para descobrir jogadores de futebol pelo nome e guardar os favoritos de cada conta.

## API utilizada

**TheSportsDB** — `https://www.thesportsdb.com/api/v1/json/3/` (chave pública de teste, sem cadastro).

- `searchplayers.php?p=<nome>` para buscar jogadores (tela Explorar);
- `lookupplayer.php?id=<id>` para abrir o detalhe de um jogador.

## Requisitos do projeto

| Requisito | Onde está |
|-----------|-----------|
| API REST (Retrofit + coroutines) | `network/` e `data/JogadorRepository`; telas com estados carregando, sucesso, sem resultados e erro de conexão |
| Login e sessão | Cadastro e login locais no Room (senha com hash SHA-256), sessão em SharedPreferences (`data/SessaoStore`), botão **Sair** no Perfil |
| Persistência local (Room) | Tabelas `usuarios` e `favoritos` (favoritos pertencem ao usuário logado) |
| Navegação | Navigation Compose com barra inferior de 3 abas (Explorar, Favoritos, Perfil) e rota type-safe `RotaDetalhe(jogadorId)` |

## Organização em pacotes

```
com.onze.app
 ├─ network        TheSportsDbApi (interface do Retrofit) e RetrofitClient
 ├─ data           data classes, Room (Usuario, Favorito, DAOs, AppDatabase), repositórios e sessão
 ├─ viewmodel      ViewModels, estados de tela e fábricas
 └─ userinterface  telas Compose, navegação (OnzeApp, Rotas) e MainActivity
```

Regra de Dependência (as duas buscas globais voltam vazias):
`import retrofit2` dentro de `userinterface` e `import ...userinterface` dentro de `viewmodel`.

## Prints

(adicionar de 4 a 6 imagens em `docs/`: login, cadastro, lista, detalhe, favoritos, estado de erro)

| Login | Cadastro | Explorar |
|-------|----------|----------|
| ![login](docs/login.png) | ![cadastro](docs/cadastro.png) | ![explorar](docs/explorar.png) |

| Detalhe | Favoritos | Estado de erro |
|---------|-----------|----------------|
| ![detalhe](docs/detalhe.png) | ![favoritos](docs/favoritos.png) | ![erro](docs/erro.png) |

## Divisão de tarefas

| Integrante | O que fez |
|------------|-----------|
| (nome 1) | `network` e `data`: API, Room, Repository, usuários |
| (nome 2) | `viewmodel` e `userinterface`: telas e navegação |
| (nome 3, se houver) | Login e sessão |

## O que aprendemos

- (nome 1): (uma frase sobre a sua parte)
- (nome 2): (uma frase sobre a sua parte)

## Como rodar

1. Abra a pasta no Android Studio (Ladybug ou mais novo) e aguarde o Gradle sincronizar.
2. Rode no emulador (API 24+) com internet.
3. Crie uma conta, entre, busque por "Danny Welbeck" e favorite.

Para trocar o nome do pacote (`com.onze.app`), altere `namespace` e `applicationId` em `app/build.gradle.kts` e renomeie a pasta do pacote com o refactor do Android Studio.
