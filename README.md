**Hello World**, sejam bem vindos ao meu artigo relacionado à testes de mutação.

## Agradecimentos especiais:

Antes de iniciarmos, gostaria muito de agradecer ao meu amigo de trabalho [**Paulo Ricardo**](https://www.linkedin.com/in/pricardoti/) pelo incentivo de estar escrevendo esse artigo e poder compartilhar a minha visão dessa ferramenta com o restante de vocês.

## Afinal, o que é o Teste de Mutação?

![image](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/74xrpgja3vl50ijpvxfx.png) 

O Pitest faz uma mutação no seu projeto, ele permite que você garanta uma margem de cobertura real dentro dos seus testes fazendo uma “cópia” do projeto e inserindo erros para ver se o seus testes irão falhar após a mutação. Quando o teste falha o mutante é morto. Caso algum mutante sobreviva isso significa que você precisa fazer mais testes unitários, os mutantes vivos servem de input para criar mais testes.

## Ok, mas como isso me ajuda com a qualidade do meu código?

![image](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/lswmr2pp6tnea5hs8k1n.png) 

Devido as clássicas correrias que os desenvolvedores precisam entrar para entregar um determinado projeto, acaba que o teste é feito mais para poder aumentar a cobertura do código, só que isso não é o correto, os testes vem com o objetivo de fornecer mais a qualidade do que você está entregando para algum cliente ou consumidor e também documentar para que outro desenvolvedor entenda melhor o seu código, mais pra frente vocês irão entender em que ponto quero chegar :)

## Quais tecnologias iremos utilizar?

![image](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/gwddh9mzr7fub83gdvag.png) 

Para esse artigo em questão, iremos precisar ter as seguintes tecnologias no nosso projeto:

1. [Java 8+](https://www.java.com/pt-BR/download/help/whatis_java.html): Linguagem de Programação Java na versão 8+
2. [Junit 5](https://junit.org/junit5/): Framework para desenvolvimento de testes
3. [Maven](https://maven.apache.org/): Ferramenta de automação para compilação
4. [Pitest](https://pitest.org/): Ferramenta para realizar os testes mutantes
5. [Spring](https://spring.io/): Framework de programação

Não irei entrar muito a dentro de todas as tecnologias utilizadas pois não é o nosso objetivo desse artigo.

## Configurando o nosso projeto:

![image](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/vc5dbouy55a2xq6iyn5z.png) 

Para que possamos utilizaf o **pitest**, precisamos fazer o uso do Junit 4 ou superior, mas como foi dito anteriormente, iremos utilizar o **Junit5**:

```xml
<dependency>
    <groupId>org.junit.platform</groupId>
    <artifactId>junit-platform-launcher</artifactId>
    <version>1.7.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.7.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.vintage</groupId>
    <artifactId>junit-vintage-engine</artifactId>
    <version>5.7.1</version>
    <scope>test</scope>
</dependency>
```
Também será necessário que a gente adicione no nosso projeto o plugin do **pitest** que é o objetivo principal do nosso artigo:

```xml
            <plugin>
                <groupId>org.pitest</groupId>
                <artifactId>pitest-maven</artifactId>
                <version>1.4.11</version>
                <dependencies>
                    <dependency>
                        <groupId>org.pitest</groupId>
                        <artifactId>pitest-junit5-plugin</artifactId>
                        <version>0.8</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <targetClasses>
                        <param>SeuPacoteDeClasse.*</param>
                    </targetClasses>
                    <targetTests>
                        <param>SeuPacoteDeTeste.*</param>
                    </targetTests>
                </configuration>
            </plugin>
```
**Observação**: Existem outras configurações que você pode inserir no plugin conforme a sua preferência, como por exemplo, excluir uma determinada classe ou pacote que não irá ser testado:

```xml
<excludedClasses>
 <param>PacoteDasSuasClasses.*</param>
 <param>PacoteDasSuasClasses.SouUmaClasse</param>
</excludedClasses>
```
Só espero que você não exclua o projeto inteiro, ok? :D

## Revelando a falha de fazer teste apenas para cobertura:

![image](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/9txnkmi5av2hmkoojqoq.png)

Conforme foi dito anteriormente, devido à alguma correria ou até mesmo por falta de conhecimento, o desenvolvedor costuma fazer o teste apenas pela cobertura ou acaba esquecendo de inserir alguma validação importante, então vamos analisar o seguinte cenário:

- Dentro do nosso projeto temos uma classe chamada **UsuarioTranslator**

- No nosso método receberemos como parâmetros uma String(nome) e um Integer(idade)

- Após isso iremos fazer a instância da classe **Usuario** e iremos settar os parâmetros recebidos em seus respectivos atributos

- Logo em seguida, iremos retornar o usuario

```java
public final class UsuarioTranslator {

    private UsuarioTranslator() {}
    
    public static Usuario of(final String nome, final Integer idade) {
        final Usuario usuario = new Usuario();
        
        usuario.setNome(nome);
        usuario.setIdade(idade);
        
        return usuario;
    }
}
```
Ok, após entendermos o objetivo do nosso método, vamos para a nossa classe de teste, **UsuarioTranslatorTest**:
```java
class UsuarioTranslatorTest {

    @Test
    @DisplayName("Deverá retornar Usuario - Quando sucesso")
    void of_Usuario_QuandoSucesso() {
        final Integer idade = 10;
        final String nome = "Dudu";

        final Usuario usuario = UsuarioTranslator.of(nome, idade);

        Assertions.assertNotNull(usuario);
        Assertions.assertEquals(nome, usuario.getNome());
    }
}
```
Como podemos ver, fizemos a chamada do método da nossa classe e fizemos as seguintes verificações:
- Verificamos que o objeto de retorno não está nulo 
- Verificamos que *nome** esperado é igual ao nome informado no **usuario**

Vamos conferir a nossa cobertura?

![Captura de tela de 2021-03-16 23-31-44](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/0p5429d58pj15pnawxl5.png)

Mas afinal, nesse exemplo simples não tivemos nenhum problema, onde estava o nosso erro?

Exatamente, esquecemos de fazer a validação se o atributo de **idade** foi settado corretamente, vamos ver o que o **Pitest** tem a dizer?

## Entrando mais na ferramenta:

![image](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/8teleik9kxgyusxvhci8.png)

Para que possamos executar o **Pitest** basta executar o seguinte comando no terminal do seu projeto:

```xml
mvn org.pitest:pitest-maven:mutationCoverage

```
Ou executar o plugin diretamente na sua IDE clicando na opção **pitest:mutationCoverage**, a escolha é sua :)

![Captura de tela de 2021-03-16 23-41-57](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/h1ke2dm7umue1kxfbvbq.png)

Após fazer a execução e tiver configurado direitinho o seu projeto até aqui, você irá ter o seguinte resultado:

![Captura de tela de 2021-03-16 23-51-46](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/xfv8gcr776fmuroanng5.png) 
 
Como podemos ver acima, o **Pitest** fez a geração de 5 mutações e com o nosso teste conseguimos matar apenas **3**, mas não se preocupe, para que você tenha uma visão mais analítica, o **Pitest** gera pra você um relatório, para que possamos acessar o mesmo basta seguir o seguinte caminho: 
- target 
- pit-reports 
- index.html

![Captura de tela de 2021-03-16 23-54-32](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/6vqjsjpsrlyi9g4aljuj.png)

Como podemos ver, o **Pitest** tirou a nossa linha do usuario.setIdade(idade); e disse que se removermos a chamada dessa linha, o resultado será o mesmo e sabemos que isso não é verdade, correto? :) 

Então agora basta voltarmos na nossa classe de teste e fazer as suas devidas alterações.

## Obrigado pessoal:

![image](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/utjsstdcrxflznbq5mx9.png)
 
Agradeço a todos vocês por terem lido o meu artigo, como foi dito esse foi o primeiro de muitos que virão, então caso tenham gostado ou tenha um comentário para que possa estar me apoiando e me incentivando ficarei extremamente grato :)

Agora deixo um desafio especial para que você execute o **Pitest** no seu projeto e diga qual foi a sua reação quando viu o resultado :D

**Observação:** Na minha primeira vez não foi muito positivo, mas me ajudou a ter uma qualidade maior nos projetos :D

- Linkedin: https://www.linkedin.com/in/gabriel-augusto-1b4914145/
- Dev.to: https://dev.to/gabrielaugusto1996/testes-de-mutacao-garanta-ja-a-qualidade-do-seu-codigo-16gn
 
