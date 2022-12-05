# Assignment 9 - Mutation Testing (White-box Testing)

// TODO: introdução, definir Mutation Testing, Pitest, Equivalent Mutants, etc
// TODO: falar da configuração do Pitest

## 1) Initial Mutation Score

// TODO: exclude classes/mutants in classes related to the GUI
// TODO: descrever mutation score dos unit tests feitos nos assignments anteriores ("Analyze which mutants survive to your unit test cases and which parts of the source code has the most not-killed mutants")

![Initial Mutation Score](./images/mt_initial_score.png)

// TODO: tirar print à coverage das classes

## 2) Equivalent Mutants

// TODO: If any exists, list them here

Linha 163 -> Project.java
Linha 177 -> Project.java
Linha 185 -> Project.java
Linha 193 -> Project.java
Linha 201 -> Project.java

Linha 63 -> ProjectSerializer.java (bug já foi fixed, portanto ter aquela call ou não é igual)
Linha 67 -> ProjectSerializer.java (valor default de ENCODING já é "encoding", que é igual a "UTF-8")
Linha 69 -> ProjectSerializer.java (valor default de INDENT já é "indent", que é igual a "yes")
Linha 71 -> ProjectSerializer.java (startDocument é um unnecessary event - link stackoverflow?)
Linha 74 -> ProjectSerializer.java (não faz diferença no parsing, etc: "XML declaration contains details that prepare an XML processor to parse the XML document. It is optional, but when used, it must appear in the first line of the XML document." - https://www.tutorialspoint.com/xml/xml_declaration.htm)
Linha 87, 92, 99 -> ProjectSerializer.java (clear apenas liberta memória, não afeta comportamento do programa - https://docs.oracle.com/javase/7/docs/api/org/xml/sax/helpers/AttributesImpl.html#clear())
Linha 111 -> ProjectSerializer.java (endDocument é um unnecessary event - link stackoverflow?)
Linha 113 -> ProjectSerializer.java (flush apenas libera a stream, não afeta o comportamento do programa - https://www.tutorialspoint.com/java/io/outputstreamwriter_flush.htm)
Linha 114 -> ProjectSerializer.java (close apenas fecha a stream, não afeta o comportamento do programa - https://www.tutorialspoint.com/java/io/outputstreamwriter_close.htm - Therefore, if we forget to close the stream, the underlying channel will remain open and then we would end up with a resource leak - No, the topmost level Stream or reader will ensure that all underlying streams / readers are closed.)
Linha 193 -> ProjectSerializer.java (é apenas print na consola, pelo que não afeta o comportamento do programa)
Linha 204 -> ProjectSerializer.java (quando attributes é null, por default é considerado como um AttributesImpl vazio - If there are no attributes, it shall be an empty Attributes object - https://docs.oracle.com/javase/7/docs/api/org/xml/sax/ContentHandler.html#startElement(java.lang.String,%20java.lang.String,%20java.lang.String,%20org.xml.sax.Attributes))

// TODO: ProjectTableModel.java -> estas serão Equivalent? A 205 foi corrigida... (linhas 210, 218)

## 3) Unit Tests

// TODO: Brief description of test cases developed to increase project’s mutation score.

Project.java -> matamos aqueles 2 mutantes dos times

ProjectSerializer.java -> matamos o mutante das quotas no writeXML (linha 95)
ProjectSerializer.java -> matamos o mutante do time started (linha 146)
ProjectSerializer.java -> matamos o mutante do quota overall e quota today (linha 165, 167)

ProjectTableModel.java -> matamos o mutante ao adicionar testes ao logger (linha 160, 183, 187, 205 -> este acho que é killed porque notifica os listeners, que envolvem logs)
ProjectTableModel.java -> matamos o mutante ao adicionar teste que verifica se o printstackstrace foi printed (linha 191, 192)

// TODO: ProjectTime.java -> o construtor private não deve ser para testar, certo?

## 4) Final Mutation Score

// TODO: descrever score final de Mutation

-----

## Group 10

- Hugo Guimarães, up201806490
- Paulo Ribeiro, up201806505

## Sources

- [Class Slides - Prof. José Campos](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/lectures/lecture-8.pdf)