# Protótipo de aplicativo Android para trabalho de conclusão de curso.
* **Sobre**

Protótipo de aplicativo **Android**, para trabalho de conclusão de curso, com consumo de **WebService** por servidor de aplicação JBoss, hospedado na Amazon e gerenciado pelo **OpenShift** em [OpenWSTCC](http://openwstcc-devbr.rhcloud.com/). Você também pode verificar a cópia local do Servidor de Aplicação [clicando aqui (wstcc)](https://github.com/gabrielqueiroz/wstcc).

O aplicativo utiliza recursos recentes do Android, visando melhor performance e usabilidade, tais como, RecyclerView, CardView, ExpandableList, NavigationDrawer entre outros. Para utilizar o aplicativo é necessário ter acesso com a internet. Possui compatibilidade a partir do Android Jelly Bean até o mais recente Marshmallow (API 16-23). 

Obs: Para uma melhor experiência, recomendamos o uso do Android Lollipop 5.0.

[Faça o Download do Aplicativo no Android (APK-Beta)](https://drive.google.com/file/d/0Bzrtyg35KT0KNXZEVG83R2c5LUE/view?pref=2&pli=1)

* **Como funciona?**

O aplicativo permite aos estudantes compartilharem suas dúvidas, como em um fórum. Nele, você pode se vincular as matérias de seus interesses, enviar novas dúvidas ou respostas, e pesquisar por dúvidas ja realizadas.

A ideia surgiu da necessidade de encontrar respostas mais condizentes as suas dúvidas, uma vez uma mesma matéria pode ser abordada de diversas maneiras por cada instituição.

Com esta ferramenta, pretendemos motivar os alunos a interagir de maneira mais prática e rapida, também possibilitando aos professores interagirem, verificando quais dúvidas seus alunos tem sobre o conteúdo.

* **O que você pode encontrar neste projeto?**

1. Utilização de Android Volley para requisições HTTP GET e POST com parametro, gerenciadas no ciclo de vida do aplicativo.
2. Utilização do Framework GSon para gerenciamento de JSon, recebendo objetos e listas.
3. Layout Material Design, focando compatibilidade com API 14 até 23 (KitKat até Marshmallow).
4. RecyclerView com utilização de OnScrollListener para ocultar ou exibir a ActionBar.
5. CardView para exibir informações de Dúvidas e Respostas, adaptando o conteudo dinamicamente.
6. ExpandableListView possuindo uma lista de checkbox a cada item expansível.
7. NavigationDrawer com animação do Lollipop para exibição do menu lateral.
8. ActionFloatingButton para criação e confirmação de novas atividades.
9. ProgressBar customizada.

* **Screenshots**

O projeto continua em andamento, mas ja está em um modelo estável. Os seguintes screenshots serão atualizados conforme novas modificações surgirem.

![View Login](https://raw.githubusercontent.com/gabrielqueiroz/androidtcc/master/screenshots/login.png)
![View Novo usuário](https://raw.githubusercontent.com/gabrielqueiroz/androidtcc/master/screenshots/novousuario.png)
![View Principal](https://raw.githubusercontent.com/gabrielqueiroz/androidtcc/master/screenshots/main.png)
![View de Pesquisa](https://raw.githubusercontent.com/gabrielqueiroz/androidtcc/master/screenshots/pesquisa.png)
![View NavigationDrawer](https://raw.githubusercontent.com/gabrielqueiroz/androidtcc/master/screenshots/navdrawer.png)
![View de Resposta](https://raw.githubusercontent.com/gabrielqueiroz/androidtcc/master/screenshots/resposta.png)
![Ajuda de Resposta](https://raw.githubusercontent.com/gabrielqueiroz/androidtcc/master/screenshots/ajuda_resposta.png)
![View de Duvida](https://raw.githubusercontent.com/gabrielqueiroz/androidtcc/master/screenshots/novaduvida.png)
![Ajuda de Duvida](https://raw.githubusercontent.com/gabrielqueiroz/androidtcc/master/screenshots/ajuda_duvida.png)
![View de Materias](https://raw.githubusercontent.com/gabrielqueiroz/androidtcc/master/screenshots/materias.png)
