## Setup

Crie um arquivo `local.properties` na raiz do projeto:

```properties
TMDB_BASE_URL=https://api.themoviedb.org/3/
TMDB_TOKEN=SEU_TOKEN
```

Você pode gerar um token em:
https://www.themoviedb.org/settings/api

# 🧪 Como Testar o Fluxo de Biometria

Para validar o fluxo completo de autenticação biométrica do MovieFlux, siga os passos abaixo:

1. **Primeiro Acesso**  
   Faça login utilizando:
   
   - Usuário: `admin` ou `user`
   - Senha: `1234`

2. **Ativação da Biometria**  
   Após o login, o aplicativo detectará automaticamente se o dispositivo possui suporte à biometria.

   Caso disponível, será exibida uma solicitação para ativar autenticação biométrica.  
   Clique em **Ativar** e valide sua digital ou reconhecimento facial.

3. **Sessão Persistente**  
   Após autenticar, o aplicativo mantém a sessão do usuário ativa.  
   Ao fechar e reabrir o app, você continuará logado automaticamente.

4. **Testando o Login Rápido**  
   Para testar novamente o fluxo biométrico, acesse a tela de **Perfil** e utilize uma das opções disponíveis:

   - **Logout (Limpar Tudo)**  
     Remove completamente os dados locais e desativa a biometria, retornando para a tela de login limpa.

   - **Logout (Manter Biometria)**  
     Encerra apenas a sessão atual, mantendo a biometria configurada para o próximo acesso.

5. **Segundo Acesso com Biometria**  
   Após utilizar a opção **Logout (Manter Biometria)**, abra o aplicativo novamente.

   Você verá o diálogo de **Login Rápido**.  
   Clique em **Validar** para iniciar a autenticação biométrica.

6. **Autenticação Biométrica**  
   O prompt nativo do Android será exibido.

   Após a validação com sucesso:
   
   - O login será realizado automaticamente
   - O usuário será redirecionado diretamente para a Home
   - Não será necessário informar usuário e senha novamente

> **Dica para Emulador:** Caso use o emulador, certifique-se de configurar uma impressão digital nas configurações do Android (`Settings -> Security -> Fingerprint`) antes de testar no app.

# 🏗️ Arquitetura e Escolhas Técnicas

O projeto foi estruturado utilizando **Clean Architecture** e **MVVM (Model-View-ViewModel)**, seguindo os princípios de SOLID para garantir testabilidade e manutenção.

### 📦 Modularização
O sistema é dividido em módulos para promover o reuso e isolamento de responsabilidades:
- `:app`: Ponto de entrada e navegação principal.
- `:feature:[name]`: Módulos de interface de usuário isolados por funcionalidade (Home, Login, Detalhes, Favoritos).
- `:core:domain`: Regras de negócio puras (UseCases e Modelos).
- `:core:data`: Implementação de repositórios, integração com Retrofit e persistência com Room.
- `:core:security`: Gerenciamento de chaves (Keystore), Biometria e Prefs criptografadas.
- `:core:designsystem`: Componentes visuais, temas e ícones reutilizáveis.
- `:core:common`: Utilitários, extensões e classes base (como o BaseResult) compartilhadas entre diversos módulos do projeto.
- `:core:model`: Centraliza os modelos de dados (POJOs/Data Classes) que circulam por todas as camadas do sistema, garantindo uma única fonte de verdade para as entidades.
- `:core:network`: Infraestrutura de rede, configuração do Retrofit, interceptors e componentes de comunicação com APIs externas..

### 🛠️ Principais Bibliotecas
- **Jetpack Compose:** UI declarativa e moderna.
- **Hilt (Dagger):** Injeção de dependência para desacoplamento de camadas.
- **Retrofit & OkHttp:** Consumo da API REST da TMDB.
- **Room:** Persistência de dados local para favoritos (Offline).
- **Kotlin Flow & Coroutines:** Processamento assíncrono e fluxos de dados reativos.
- **Jetpack Security:** Criptografia de dados sensíveis.
- **Coil:** Carregamento de imagens otimizado.

---

# 🤖 Uso de Inteligência Artificial

Neste projeto, utilizei técnicas de **Prompt Engineering** com assistência de IA para otimizar o ciclo de desenvolvimento em:

- **Refatoração de Mappers:** Automação na criação de conversores entre modelos de rede (DTO) e modelos de domínio.
- **Escrita da Documentação:** Estruturação e formatação deste README para garantir clareza técnica e visual.
- **Componentes de Design System:** Geração de boilerplate para temas e variações de botões customizados.
- **Testes Unitários:** Auxílio na criação de testes unitários para ViewModels, UseCases e regras de negócio, incluindo geração de cenários de sucesso, erro e validações de comportamento reativo utilizando Kotlin Coroutines e Flow.
---

# 🔐 Autenticação e Segurança

O MovieFlux implementa um fluxo de autenticação moderno e seguro, priorizando a experiência do usuário (UX) e a proteção de dados sensíveis através de criptografia e biometria.

---

## 📋 Login Inicial (Mock)

Para fins de demonstração e agilidade no desenvolvimento, o sistema de login utiliza credenciais mockadas integradas ao `LoginRepositoryImpl`.

A lógica de autenticação é isolada na camada de Data, simulando uma validação de backend:

```kotlin
// LoginRepositoryImpl.kt
override fun login(username: String, password: String): Boolean {
    val isValid = (username == "admin" && password == "1234") ||
                  (username == "user" && password == "1234")

    if (isValid) {
        saveSession(username)
    }

    return isValid
}
```

## 🧬 Biometria (Fingerprint & FaceID)

O aplicativo utiliza a biblioteca `androidx.biometric` para oferecer uma camada adicional de segurança.

### Funcionalidades

- **Ativação Inteligente**  
  Após o primeiro login bem-sucedido, o `LoginViewModel` detecta se o hardware do dispositivo é compatível e solicita ao usuário a ativação da autenticação biométrica para acessos futuros.

- **Prompt Customizado**  
  O aplicativo implementa o `MovieFluxBiometricPrompt`, um componente Composable responsável por gerenciar o ciclo de vida da autenticação biométrica utilizando Jetpack Compose.

```kotlin
val canAuthenticate = biometricManager.canAuthenticate()

if (!biometricEnabled && canAuthenticate) {
    _uiState.value = LoginUiState.AskToEnableBiometric
}
```

## 🚀 Login Rápido (Segundo Acesso)

Após a ativação da autenticação biométrica, o fluxo de entrada é otimizado para proporcionar uma experiência mais rápida e fluida.

Ao reabrir o aplicativo, o usuário é apresentado a um diálogo de **Login Rápido**, permitindo autenticação instantânea através de:

- Impressão digital
- Reconhecimento facial

Com isso, não é necessário redigitar usuário e senha em acessos futuros, tornando o processo de login mais prático e seguro.


## 🛡️ Armazenamento Seguro

Diferente de implementações tradicionais que utilizam `SharedPreferences` comum, o MovieFlux utiliza `Jetpack Security` com `EncryptedSharedPreferences`.

Isso garante que informações sensíveis, como estado da sessão e possíveis tokens, não sejam armazenadas em texto claro no sistema de arquivos do Android.

### Recursos de Segurança

- **AES-256**  
  Todas as chaves e valores são criptografados utilizando o padrão AES-256.

- **Android Keystore**  
  As chaves mestras de criptografia são armazenadas com segurança utilizando o hardware seguro do dispositivo.

```kotlin
private val masterKey = MasterKey.Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

private val prefs = EncryptedSharedPreferences.create(
    context,
    "movieflux_secure",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

# 🏠 Home - Filmes Populares

A tela principal do MovieFlux oferece uma experiência de navegação fluida e moderna para descoberta de filmes, integrando listagem dinâmica, busca em tempo real e gerenciamento eficiente de estados da interface.

---

## 🖼️ Listagem e Grid

Os filmes são exibidos em um grid adaptativo de 2 colunas utilizando o componente `LazyVerticalGrid` do Jetpack Compose.

Cada item da lista é renderizado através do componente customizado `MovieCard`, responsável por apresentar:

- Pôster do filme
- Título
- Status de favorito

A abordagem baseada em Compose permite uma renderização performática e reativa, garantindo uma experiência suave mesmo com grandes listas de conteúdo.

## 🔄 Paginação (Infinite Scroll)

O MovieFlux implementa uma estratégia de rolagem infinita customizada, evitando dependências externas complexas e mantendo controle total sobre o fluxo de carregamento de dados.

### Funcionalidades

- **Gatilho de Carregamento**  
  O `HomeViewModel` monitora continuamente o índice do último item visível na lista. Quando o usuário se aproxima do final do grid, uma nova página é carregada automaticamente.

- **Controle de Estado**  
  O gerenciamento das variáveis `currentPage` e `canLoadMore` evita chamadas duplicadas, requisições desnecessárias e múltiplos carregamentos simultâneos.

```kotlin
val shouldLoadMore by remember {
    derivedStateOf {
        val lastVisibleItemIndex =
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

        canLoadMore && lastVisibleItemIndex >= totalItemsCount - 4
    }
}
```

## 🔍 Busca em Tempo Real

A tela Home possui uma barra de pesquisa integrada que permite buscar filmes dinamicamente através do endpoint de busca da TMDB API.

A implementação foi construída utilizando Kotlin Flow, garantindo uma experiência reativa, performática e otimizada para consumo de rede.

### Funcionalidades

- **Debounce (`500ms`)**  
  Para evitar múltiplas requisições enquanto o usuário ainda está digitando, foi aplicado o operador `debounce(500ms)`, garantindo que a API seja chamada apenas após uma breve pausa na digitação.

- **DistinctUntilChanged**  
  O operador `distinctUntilChanged()` impede chamadas repetidas quando o termo pesquisado permanece igual, reduzindo requisições desnecessárias.

- **Atualização Reativa**  
  Sempre que um novo termo é pesquisado, a paginação é reiniciada automaticamente e uma nova lista de resultados é carregada.

```kotlin
_searchQuery
    .debounce(500)
    .distinctUntilChanged()
    .collectLatest { query ->
        fetchMovies(query) // Reinicia a paginação e busca novos resultados
    }
```

## 🎭 Estados da UI

A interface do MovieFlux reage dinamicamente aos diferentes cenários de carregamento, sucesso e falha através de um `HomeUiState` selado, garantindo previsibilidade e melhor experiência do usuário.

### Estados Implementados

- **Loading**  
  Durante o carregamento inicial dos filmes, a interface exibe um `CircularProgressIndicator` centralizado.

- **Success**  
  Quando os dados são carregados com sucesso, o grid de filmes é renderizado normalmente.  
  Caso ocorra erro durante uma paginação subsequente, um botão de **"Tentar Novamente"** é exibido ao final da lista.

- **Error**  
  Em situações de falha de conexão, timeout ou erro da API, uma mensagem amigável é apresentada ao usuário junto de uma ação de recuperação.

- **Empty**  
  Quando uma pesquisa não retorna resultados, a interface informa visualmente que nenhum filme foi encontrado.

```kotlin
when (uiState) {
    is HomeUiState.Loading -> CircularProgressIndicator()
    is HomeUiState.Success -> MovieGrid(...)
    is HomeUiState.Error -> ErrorMessage(onRetry)
    is HomeUiState.Empty -> Text("Nenhum filme encontrado")
}
```

# 🎬 Detalhes do Filme

A tela de detalhes oferece uma experiência completa e imersiva, consolidando informações técnicas, identidade visual e ações de interação relacionadas ao filme selecionado.

---

## ℹ️ Informações Detalhadas

A interface foi projetada para destacar os principais elementos visuais e informativos da obra, proporcionando uma navegação rica e intuitiva.

### Recursos da Tela

- **Poster em Alta Resolução**  
  Utilização do `backdrop_path` da TMDB API no tamanho `w780`, garantindo imagens de alta qualidade e uma experiência visual mais imersiva.

- **Métricas de Avaliação**  
  Exibição da nota média do filme (`rating`) acompanhada de ícones estilizados para facilitar leitura e identificação visual.

- **Sinopse Completa**  
  Apresentação detalhada da descrição da obra, permitindo ao usuário compreender melhor a narrativa e o contexto do filme.

## 🏷️ Mapeamento de Gêneros

Como a TMDB API frequentemente retorna apenas os IDs dos gêneros em determinadas listagens, o MovieFlux implementa uma estratégia de mapeamento dinâmico para garantir a exibição correta dos nomes dos gêneros na interface.

### Estratégia Implementada

O `MovieDetailViewModel` realiza um fallback inteligente:

1. Verifica se o objeto do filme já possui os nomes dos gêneros preenchidos.
2. Caso contrário, solicita a lista global de gêneros da API.
3. Cria um mapa (`id -> gênero`) para associação rápida.
4. Atualiza dinamicamente os gêneros do filme antes de renderizar a UI.

Essa abordagem garante consistência visual e evita informações incompletas na tela de detalhes.

```kotlin
// MovieDetailViewModel.kt - Fallback para mapeamento de gêneros
private fun fetchGenresAndMap(movie: MovieDetailResponse) {
    viewModelScope.launch {
        getGenresUseCase.execute().collect { result ->

            if (result is BaseResult.Success) {

                val genreMap = result.data.genres.associateBy { it.id }

                val updatedGenres = movie.genres.map { genre ->
                    genreMap[genre.id] ?: genre
                }

                _detailState.update {
                    MovieDetailUiState.Success(
                        movieDetail = movie.copy(
                            genres = updatedGenres
                        )
                    )
                }
            }
        }
    }
}
```

### Funcionalidades

- **Favoritar / Desfavoritar**  
  Integração direta com o banco de dados local através do `ToggleFavoriteUseCase`.

  O estado visual do ícone de favorito (preenchido ou contorno) é atualizado em tempo real utilizando `Flow`, garantindo sincronização reativa entre a interface e a camada de dados.

- **Compartilhamento Nativo**  
  Implementação do compartilhamento nativo do Android utilizando `Intent.ACTION_SEND`.

  O usuário pode compartilhar rapidamente o título e a sinopse do filme com outros aplicativos instalados no dispositivo, como WhatsApp, Telegram, Gmail e redes sociais.

```kotlin
private fun shareMovie(
    context: Context,
    movie: MovieDetailResponse
) {

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"

        putExtra(Intent.EXTRA_SUBJECT, movie.title)

        putExtra(
            Intent.EXTRA_TEXT,
            "Confira este filme: ${movie.title}\n\n${movie.overview}"
        )
    }

    context.startActivity(
        Intent.createChooser(intent, "Compartilhar filme")
    )
}
```

# ❤️ Favoritos (Offline)

O MovieFlux adota uma estratégia de favoritos **Offline**, garantindo que os filmes favoritados pelo usuário permaneçam acessíveis mesmo sem conexão com a internet.

Essa abordagem melhora a experiência do usuário, reduz dependência de rede e proporciona carregamentos instantâneos da lista de favoritos.

---

## 💾 Persistência com Room

A persistência local é implementada utilizando a biblioteca `Room`, responsável por abstrair o acesso ao banco de dados SQLite local de forma segura e reativa.

Quando um filme é favoritado:

1. O objeto é convertido para uma `MovieEntity`
2. Os dados são persistidos localmente
3. A interface é atualizada automaticamente através de `Flow`

Dessa forma, a lista de favoritos pode ser carregada instantaneamente, mesmo em cenários offline.

```kotlin
@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_movies")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteFavoriteMovie(movie: MovieEntity)
}
```

## 🔄 Sincronização Reativa (SSOT)

Para garantir que o estado de favoritos permaneça consistente em todas as telas da aplicação, o MovieFlux utiliza o conceito de **Single Source of Truth (SSOT)**.

Nesse modelo, o banco de dados local se torna a única fonte confiável de verdade para o estado dos filmes favoritados.

---

### ⚡ Estratégia Implementada

- **Observação Reativa com Flow**  
  Os `ViewModels` observam continuamente alterações no banco de dados utilizando `Kotlin Flow`.

- **Atualização Automática da UI**  
  Sempre que um filme é favoritado ou removido dos favoritos, o `Room` dispara automaticamente atualizações para todos os coletores ativos.

- **Sincronização Global**  
  Isso garante que todas as telas da aplicação permaneçam sincronizadas em tempo real:

    - Home
    - Tela de Detalhes
    - Lista de Favoritos

Por exemplo, ao desfavoritar um filme na tela de detalhes, o ícone da Home e a lista de favoritos são atualizados instantaneamente, sem necessidade de recarregar a interface manualmente.

```kotlin
val uiState: StateFlow<HomeUiState> = combine(
    _movies,
    getFavoriteMovieIdsUseCase() // Observa IDs favoritados no DB
) { movies, favoriteIds ->

    val updatedMovies = movies.map { movie ->
        movie.copy(
            isFavorite = favoriteIds.contains(movie.id)
        )
    }

    // ... atualiza o estado da UI
}
```

## 📑 Tela de Favoritos

O MovieFlux possui uma aba exclusiva para gerenciamento dos filmes favoritados, proporcionando acesso rápido e organizado ao conteúdo salvo localmente.

---

### Funcionalidades

- **Acesso Rápido**  
  A tela lista automaticamente todos os filmes armazenados no banco de dados local, permitindo acesso instantâneo mesmo sem conexão com a internet.

- **Gestão Direta dos Favoritos**  
  O usuário pode adicionar ou remover filmes diretamente da lista através do `ToggleFavoriteUseCase`.

  Graças à arquitetura reativa baseada em `Flow` + `Room`, qualquer alteração é refletida imediatamente na interface, sem necessidade de atualização manual da tela.

- **Estado Vazio (Empty State)**  
  Caso o usuário ainda não possua filmes favoritados, a interface apresenta um estado vazio amigável, incentivando a descoberta e salvamento de novos filmes.
