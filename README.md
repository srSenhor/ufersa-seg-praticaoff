## Prática Offline 2 - Banco Digital

Este projeto, desenvolvido na disciplina de Segurança Computacional da UFERSA, tem como objetivo pôr em prática o conteúdo passado em aula sobre tecnologias vistas na disciplina - nesse caso algoritmos de criptografia simétrica e assimétrica para obter confidencialidade, autenticidade, integridade e irretratabilidade.


### Interpretação do problema

O objetivo do projeto é fazer uma simulação de um sistema cliente/servidor para uma banco digital simplificado, no qual é possível um cliente realizar operações comuns a um banco, como saque, depósito e transferências, usando criptografia nas comunicações.

### Especificações do sistema

- Todo cliente deve possuir:
    - Cadastro de Pessoa Física (CPF)
    - Nome
    - Endereço
    - Telefone

- O sistema deve ter/permitir:

    - **Autenticação de usuários**: usando o número da conta e uma senha, um usuário legítimo pode entrar no sistema e usar as demais funcionalidades.
    - **Autenticação de mensagens**:
    - **Criar conta corrente**: o usuário deve abrir uma conta para poder usar o sistema.
    - **Realizar saque**: um usuário legítimo deve conseguir realizar saques da sua conta desde que essa não esteja vazia.
    - **Realizar depósito**: um usuário legítimo deve conseguir realizar depósitos na sua conta.
    - **Realizar transferência**: um usuário legítimo deve conseguir realizar transferência para outra conta cadastrada
    - **Verificar saldo**: um usuário legítimo deve conseguir verificar o saldo em sua conta.
    - **Realizar/acompanhar investimentos**: um usuário legítimo deve conseguir realizar investimentos na poupança (rende 0,5% ao mês, criado junto a criação da conta corrente) e na rende fixa (rende 1,5% ao mês, com o correntista podendo escolher investir), além de ver a projeção do rendimento para três, seis e doze meses.

#### Detalhes dos requisitos
- As chaves para para encriptação e autenticação (públicas e privadas) devem ser distribuídas usando algoritmos assimétricos, como Diffie-Hellman-Merkle ou <span style="color:pink">RSA - trabalharemos com RSA </span> - ou usando um servidor auxiliar para gerar e distribuir as chaves.
- O sistema do banco deve garantir que todas as trocas de mensagem usem da criptografia híbrida, no caso desse projeto o AES (simétrica) junto ao RSA (assimétrica) e codificadas em Base64.
- Os algoritmos assimétricos, no caso o RSA, deve ser implementados manualmente para fins de estudo. <span style="color:red">**NÃO USAR ALGORITMO DISPONIBILIZADO PELA API DA LINGUAGEM** (Java)<span>.
- Todas as *user stories* devem estar implementadas e funcionais.
- Deve haver uma simulação de ataque onde um cliente, simulando um atacante, deve realizar requisições ao sistema, que prontamente deve descartar as mensagens que não forem autenticadas.
- Ao iniciar o sistema, para fins de estudo, o servidor inicializa com três contas criadas.

### Tecnologias utilizadas
- Visual Studio Code
- Java SE 17