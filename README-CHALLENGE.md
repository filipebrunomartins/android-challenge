# Android Technical Challenge: MovieFlux

## 📌 Visão Geral
O objetivo deste desafio é avaliar as suas competências técnicas em desenvolvimento Android Nativo, especificamente em arquitetura, segurança, consumo de APIs, persistência de dados e qualidade de código.

Você deverá implementar um aplicativo que liste filmes populares utilizando a **API V3 do TheMovieDB (TMDB)**, precedido de um fluxo de autenticação.

## 🚀 Requisitos Funcionais

### 1. Autenticação e Segurança (Novo)
- **Login Inicial:** Tela de login simples com Usuário e Senha (pode ser mockado, sem necessidade de backend real para validação, exemplo: usuário "admin", senha "1234").
- **Biometria (Fingerprint/FaceID):** Após o primeiro login com sucesso, perguntar se o usuário deseja ativar a autenticação biométrica para os próximos acessos.
- **Segundo Acesso:** Se a biometria estiver ativa, solicitar a impressão digital/facial na abertura do app para contornar a digitação de senha.
- **Segurança (Diferencial):** Armazenamento seguro de credenciais ou tokens (ex: EncryptedSharedPreferences, Keystore).

### 2. Home - Filmes Populares
- **Listagem:** Exibir uma lista ou grid dos filmes populares.
- **Paginação:** Implementar rolagem infinita (Infinite Scroll) para carregar novas páginas da API.
- **Busca:** Barra de pesquisa funcional para filtrar filmes por título (endpoint de `search`).
- **Estados da UI:** Tratamento visual para Loading, Erro (conexão ou API) e Lista Vazia (sem resultados na busca).

### 3. Detalhes do Filme
- **Informações:** Exibir o poster (tamanho grande), título, nota (rating) e sinopse.
- **Gêneros:** Exibir os gêneros por extenso (ex: Ação, Aventura, Terror). *Nota: A API de filmes retorna apenas IDs; você precisará mapeá-los usando o endpoint de gêneros.*
- **Ações:** Botão para favoritar/desfavoritar e botão para partilhar o link/imagem do filme.

### 4. Favoritos (Offline First)
- **Persistência:** Os filmes favoritados devem ser guardados localmente para consulta offline.
- **Sincronização:** O estado de "favorito" deve estar sincronizado em todas as telas (se favoritar nos detalhes, deve aparecer como tal na Home).
- **Lista:** Uma aba dedicada para listar apenas os filmes guardados pelo utilizador.

## 🛠 Requisitos Técnicos

### Essenciais
- **Linguagem:** Kotlin.
- **Arquitetura:** MVVM (Model-View-ViewModel).
- **Asincronismo:** Uso de Flow e Coroutines.
- **Injeção de Dependência:** Hilt, Koin ou Dagger.
- **Networking:** Retrofit ou Ktor.
- **Banco de Dados:** Room ou Realm.
- **Segurança:** AndroidX Biometric Library.
- **Testes:** Testes unitários (ViewModels e Repositories).

### Diferenciais
- **UI:** Implementação em Jetpack Compose.
- **Arquitetura:** Modularização por features ou camadas (Clean Architecture).
- **Design:** Uso de componentes reutilizáveis e atenção ao sistema de cores (Sugerimos o uso de **Teal Green** como cor primária).
- **Testes:** Testes de interface (Espresso ou Compose UI Test).
- **Segurança Avançada:** Uso de EncryptedSharedPreferences para guardar a preferência de biometria ou dados sensíveis mockados.

## 📡 Integração com API
Utilize a [API V3 do TheMovieDB](https://developer.themoviedb.org/reference/intro/getting-started).
- **Endpoints sugeridos:**
    - `GET /movie/popular`
    - `GET /search/movie`
    - `GET /genre/movie/list`
    - `GET /movie/{movie_id}`

## 📝 Instruções de Entrega

1. **Repositório:** Publique a solução num repositório público (GitHub/GitLab).
2. **Documentação (README):**
    - Instruções de como configurar a `API_KEY` para rodar o projeto.
    - Como testar o fluxo de biometria.
    - Explicação das decisões de arquitetura e escolha de bibliotecas.
    - Caso tenha utilizado IA (Prompt Engineering) para otimizar o desenvolvimento, documente como ela foi aplicada.
3. **Confidencialidade:** Não inclua nenhuma referência a nomes de empresas ou instituições no código ou no repositório.

## ⚖️ Critérios de Avaliação
Procuramos código limpo, testável e escalável. Pontos cruciais:
- Implementação correta da API de Biometria do AndroidX e fallback em caso de falha.
- Como lida com o mapeamento de IDs de gêneros para strings.
- Eficiência na sincronização do estado de favoritos entre ecrãs.
- Tratamento de exceções e estados de rede.
- Qualidade e cobertura dos testes unitários.