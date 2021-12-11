package banco;

import org.apache.commons.collections.list.AbstractLinkedList;

import java.text.SimpleDateFormat;
import java.util.*;

public  abstract class Conta {

    private String nome;
    private  String CPF;
    private double rendaMensal;
    private static int conta =  (int) Math.ceil(Math.random() * (50000)+10000);
    private double saldo;
    private String agencia;
    public static List<Conta> listaContas = new ArrayList<>();
    public static List<Integer> contasUsuarios = new ArrayList<>();
    public  List<String> transacoes = new ArrayList<>();
    private static List<String> listaCPF = new ArrayList<>();
    public static Scanner entrada = new Scanner(System.in);

    public Conta() {
        contasUsuarios.add(getConta());
        this.nome = this.setNome();
        this.CPF = this.setCPF();
        this.rendaMensal = this.setRendaMensal();
        this.saldo = 0;
        this.agencia = validaAgencia(agencia);
        listaContas.add(this);
        mostraConta();
        System.out.print("\nConta criada com sucesso!");
        iniciarAtendimento();
    }

    class RelatoriosTransacoes {

        public void listarContas() {
            List<Conta> contaCorrenteLista = new ArrayList<>();
            for(Conta contas: listaContas) {
                if(contas instanceof ContaCorrente) {
                    contaCorrenteLista.add(contas);
                }
            }
            List<Conta> contaPoupancaLista = new ArrayList<>();
            for(Conta contas: listaContas) {
                if(contas instanceof ContaPoupanca) {
                    contaPoupancaLista.add(contas);
                }
            }
            List<Conta> contaInvestimentoLista = new ArrayList<>();
            for(Conta contas: listaContas) {
                if(contas instanceof ContaInvestimento) {
                    contaInvestimentoLista.add(contas);
                }
            }
            String[] opcoes = {"1","2","3"};
            List<String> listaOpcoes = new ArrayList<>();
            Collections.addAll(listaOpcoes,opcoes);
            String listar;
            while(!listaOpcoes.contains(opcoes)) {
            System.out.print("\nQuais contas você gostaria de listar?\n1 - Conta Poupança\n2 - Conta Corrente" +
                    "\n3 - Conta Investimento\nq - Sair\n--> ");
            listar = entrada.nextLine();

                switch (listar) {
                    case "1":
                       if(contaPoupancaLista.size()==0){
                           System.out.println("Não há contas do tipo poupança disponíveis para listar");
                       }else{
                           for(Conta contas: contaPoupancaLista) {
                               contas.extrato();
                           }
                       }
                       break;
                    case "2":
                        if(contaCorrenteLista.size()==0){
                            System.out.print("\nNão há contas corrente disponíveis para listar");
                        }else{
                            for(Conta contas: contaCorrenteLista) {
                                contas.extrato();
                            }
                        }
                        break;
                    case "3":
                        if(contaInvestimentoLista.size()==0){
                            System.out.print("\nNão há contas de investimento disponíveis para listar");
                        }else{
                            for(Conta contas: contaPoupancaLista) {
                                contas.extrato();
                            }
                        }
                        break;
                    case "q": case "Q":
                        return;
                    default:
                        System.out.print("\nNão entendi o que você quis dizer com isso.");
                        break;
                }
            }

        }

        public void contasNegativo() {
            List<Conta> contasNegativo = new ArrayList<>();
            for(Conta contas: listaContas) {
                if(contas.getSaldo()<0) {
                    contasNegativo.add(contas);
                }
            }
            if(contasNegativo.size()==0) {
                System.out.print("Não há contas com saldo em negativo para serem mostradas");
            } else {
                for(Conta conta: contasNegativo) {
                    conta.extrato();
                }
            }
        }

        public void totalValorInvestido() {
            double totalValor = 0;
            List<Conta> contaInvestimentoLista = new ArrayList<>();
            for(Conta contas: listaContas) {
                if(contas instanceof ContaInvestimento) {
                    contaInvestimentoLista.add(contas);
                }
            }
            for(Conta conta: contaInvestimentoLista) {
                totalValor+= conta.getSaldo();
            }
            System.out.printf("\nO valor total investido é de %.2f",totalValor);
        }

        public void historicoTransacoes() {
            int resposta = 0;
            int transacoesDisponiveis = 0;

            for(Conta historico: listaContas) {
                transacoesDisponiveis += historico.transacoes.size();
            }
            if(transacoesDisponiveis==0) {
                System.out.print("\nNo momento não há históricos disponíveis para verificação.");
                return;
            }
            while(!contasUsuarios.contains(resposta)) {
                System.out.print("\nDigite o nº da conta que você gostaria de listar o histórico das transações\n" +
                        "Contas disponíveis para verificar histórico:\n");
                for(int i = 0;i<contasUsuarios.size();i++) {
                    System.out.print((i+1)+") "+contasUsuarios.get(i)+"\n");
                }
                System.out.print("\n--> ");
                resposta = Integer.parseInt(entrada.nextLine());
                if(!contasUsuarios.contains(resposta)) {
                    System.out.println("Valor inválido ou número da conta inexistente.");
                } else if(listaContas.get(contasUsuarios.indexOf(resposta)).transacoes.size()==0) {
                    System.out.println("Não há um histórico de transações disponíveis para essa conta, porém existe em pelo menos uma das contas" +
                            " listadas.");
                } else {
                    for(String historico: listaContas.get(contasUsuarios.indexOf(resposta)).transacoes){
                        System.out.print(historico);
                    }
                }
            }
        }
    }



    private String validaCPF(String CPF) {
        final String CPF_TEMPLATE = "\\d\\d\\d.\\d\\d\\d.\\d\\d\\d-\\d\\d";

        boolean valido = false;

        while(!valido) {

            if(CPF.length()!=14){
                System.out.print("\nNúmeros de caracteres inválido. Insira o CPF novamente: ");
                CPF = entrada.nextLine();
            } else if(!(CPF.matches(CPF_TEMPLATE))){
                System.out.print("\nCPF com caracteres inválidos. Insira novamente conforme solicitado (xxx.xxx.xxx-xx): ");
                CPF = entrada.nextLine();
            } else if(listaCPF.contains(CPF)) {
                System.out.print("\nEsse cpf já está contido em nossa base de dados. Digite novamente: ");
                CPF = entrada.nextLine();
            } else {
                valido = true;
            }
        }
        return CPF;
    }


    private String validaAgencia(String agencia) {
        String[] agencias = {"001","002"};
        List<String> listaAgencias = new ArrayList<>();
        Collections.addAll(listaAgencias,agencias);

       while(!(listaAgencias.contains(agencia))) {
                System.out.print("As agências disponíveis são:" +
                        "\n001 - Florianópolis\n002 - São José\n\n" +
                        "Digite o número de sua agência: ");
                agencia = entrada.nextLine();
            }
        return agencia;
    }


    public String getNome() {
        return nome;
    }

    public String getCPF() {
        return CPF;
    }

    public double getRendaMensal() {
        return rendaMensal;
    }

    private int getConta() {
        return conta++;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getAgencia() {
        return agencia;
    }

    public Scanner getEntrada() {
        return entrada;
    }

    private String  setNome() {
        System.out.print("\nInsira o seu nome: ");
        this.nome = entrada.nextLine();
        return nome;
    }

    private String setCPF() {
        System.out.print("\nDigite seu CPF: ");
        this.CPF = validaCPF(entrada.nextLine());
        listaCPF.add(CPF);
        return CPF;
    }

    private double setRendaMensal() {
        System.out.print("\nDigite sua renda mensal: ");
        this.rendaMensal = Integer.parseInt(entrada.nextLine());
        return rendaMensal;
    }

    public void mostraConta() {
        System.out.print("A conta é um número sequencial gerada automáticamente. O número de sua conta é: " +
        contasUsuarios.get(listaContas.indexOf(this)));
    }


    public double saque() {
        double valor;
        System.out.print("\nDigite o valor a ser sacado: ");
        valor = Double.parseDouble(entrada.nextLine());
        if(valor>0&&this.saldo-valor >= 0) {
            this.saldo-=valor;
            System.out.printf("\n Valor de %.2f sacado com sucesso! Seu saldo agora é de %.2f",valor,this.saldo);
            transacoes.add("\n------- Historico de Transação -------\n" +
                    "\ntipo de transação: saque" +
                    "\nnome: " + this.getNome() +
                    "\nagência: " + this.getAgencia() +
                    "\nconta: "+
                    contasUsuarios.get(listaContas.indexOf(this))+
                     "\nValor sacado: " + valor
                    +"\nData: "+ new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())
                    +"\nHora: "+ new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime())+"\n");
            return this.saldo;
        } else {
            System.out.print("\nValor não autorizado para saque.");
            return this.saldo;
        }
    }


    public double saqueCorrente() {
        //Limite definido como 15% do valor da renda mensal
        double limite = (this.getRendaMensal()/100)*15;
        double valor;
        System.out.print("\nDigite o valor a ser sacado: ");
        valor = Double.parseDouble(entrada.nextLine());
        if(valor>0&&(this.saldo-valor) >= -limite) {
            this.saldo-=valor;
            System.out.printf("\n Valor de %.2f sacado com sucesso! Seu saldo agora é de %.2f",valor,this.saldo);
            transacoes.add("\n------- Historico de Transação -------\n" +
                    "\ntipo de transação: saque" +
                    "\nnome: " + this.getNome() +
                    "\nagência: " + this.getAgencia() +
                    "\nconta: "+
                    contasUsuarios.get(listaContas.indexOf(this))+
                    "\nValor sacado: " + valor
                    +"\nData: "+ new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())
                    +"\nHora: "+ new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime())+"\n");
            return this.saldo;
        } else {
            System.out.print("\nValor não autorizado para saque.");
            return this.saldo;
        }
    }

    public void deposito() {
        double valor = 0;
        while(valor<=0) {
        System.out.print("\nDigite o valor a ser feito o depósito: ");
        valor = Double.parseDouble(entrada.nextLine());
            if(valor<=0) {
                System.out.print("\nValor inválido.");
            } else {
                this.saldo+=valor;
                transacoes.add("\n------- Historico de Transação -------\n" +
                        "\ntipo de transação: depósito" +
                        "\nconta: "+
                        contasUsuarios.get(listaContas.indexOf(this))+
                        "\nValor depositado: " + valor
                        +"\nData: "+ new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())
                        +"\nHora: "+ new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime())+"\n");
                System.out.printf("\nValor de %.2f depositado com sucesso!",valor);
            }
        }
    }



    public void saldo(){
        System.out.printf("\nSeu saldo é de %.2f",this.saldo);
        transacoes.add("\n------- Historico de Transação -------\n" +
                "\ntipo de transação: verificação de saldo" +
                "\nnome: " + this.getNome() +
                "\nagência: " + this.getAgencia() +
                "\nconta: "+ contasUsuarios.get(listaContas.indexOf(this))
                +"\nData: "+ new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())
                +"\nHora: "+ new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime())+"\n");
    }

    public void extrato() {
         System.out.printf("------------ EXTRATO BANCÁRIO ------------\nnome: %s\nCPF: %s\nagencia: %s\nconta: %d\nsaldo: %.2f\n",
                 this.getNome(),this.getCPF(),this.getAgencia(),contasUsuarios.get(listaContas.indexOf(this)),this.getSaldo());

    }

    public void transferir() {
        boolean transferido = false;
        double valor;
        String agenciaDestino;
        int contaDestino;

        List<String> agenciasUsuarios = new ArrayList<>();
        listaContas.forEach(c-> agenciasUsuarios.add(c.getAgencia()));

        while(!transferido) {
                System.out.printf("\nDigite o valor a ser transferido: ");
                valor = Double.parseDouble(new Scanner(System.in).nextLine());
            if(valor>0&&valor<=this.getSaldo()) {
                if(agenciasUsuarios.size()==1) {
                    System.out.printf("\nAgências disponíveis para tranferência: %s (somente a sua)\nDICA: Crie uma" +
                            " nova conta para simular uma transferência.",agenciasUsuarios.get(0));
                    return;
                }
                System.out.printf("\nAgora, digite a agência a ser tranferido o valor de %.2f reais", valor);
                System.out.printf("\nAgências disponíveis para transferência (a sua é %s):\n",agenciasUsuarios.get(listaContas.indexOf(this)));
                for(int i=0;i<agenciasUsuarios.size();i++){
                    System.out.print((i+1)+" ) "+agenciasUsuarios.get(i)+"\n");
                }
                System.out.print("--> ");
                agenciaDestino = entrada.nextLine();
                if(agenciasUsuarios.contains(agenciaDestino)) {
                    if(contasUsuarios.size()==1) {
                        System.out.printf("\nContas disponíveis para tranferência: %d (somente a sua)\nDICA: Crie uma" +
                                " nova conta para simular uma transferência.",contasUsuarios.get(0));
                        return;
                    }
                    System.out.printf("\nContas disponíveis para transferência (a sua é %d):\n",contasUsuarios.get(listaContas.indexOf(this)));
                    for(int i=0;i<contasUsuarios.size();i++){
                            System.out.print((i+1)+" ) "+contasUsuarios.get(i)+"\n");
                        }
                    System.out.print("--> ");
                    System.out.print("\nPor último, digite a conta a ser transferida: ");

                    contaDestino = Integer.parseInt(entrada.nextLine());
                    if(contasUsuarios.contains(contaDestino)&&contaDestino!=contasUsuarios.get(listaContas.indexOf(this))) {
                        this.saldo-=valor;
                        System.out.println("\nTransação realizada com sucesso!");
                        transferido = true;

                        transacoes.add("\n------- Historico de Transação -------\n\nconta origem: "+
                                contasUsuarios.get(listaContas.indexOf(this))+
                                "\nConta destino: "+ contaDestino + "\nValor: " + valor
                                +"\nData: "+ new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())
                                +"\nHora: "+ new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime())+"\n");
                    } else {
                        System.out.println("\nNúmero da conta não encontrado dentre as contas existentes.");
                    }
                } else {
                    System.out.println("\nAgência indisponível nas contas existentes.");
                }
            } else {
                System.out.printf("\nValor inválido ou acima do saldo disponível. Seu saldo é de %.2f.\nDICA: Faça um depósito",this.getSaldo());
                return;
            }

        }
    }

    public void alterarDados() {
        System.out.print("\nDigite seu nome: ");
        this.nome = entrada.nextLine();
        System.out.print("\nDigite sua renda mensal: ");
        this.rendaMensal = Double.parseDouble(entrada.nextLine());
        this.agencia = validaAgencia(entrada.nextLine());
        transacoes.add("\n------- Historico de Transação -------\n" +
                "\ntipo de transação: alteração de dados cadastrais" +
                "\nnome: " + this.getNome() +
                "\nagência: " + this.getAgencia() +
                "\nconta: "+ contasUsuarios.get(listaContas.indexOf(this))+
                "\nData: "+ new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())
                +"\nHora: "+ new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime())+"\n");
        System.out.println("\nDados atualizados com sucesso!");
    }

    public static void criarConta() {
        Scanner entrada = new Scanner(System.in);
        String[] respostasPossiveis = {"1", "2", "3"};
        List<String> listaResposta = new ArrayList<>();
        Collections.addAll(listaResposta, respostasPossiveis);
        String resposta="";

        while (!listaResposta.contains(resposta)) {
            System.out.print("Qual tipo de conta você deseja criar?" +
                    "\n1- Conta Corrente\n2 - Conta Poupança\n3 - Conta Investimento\n--> ");
            resposta = entrada.nextLine();

            switch (resposta) {
                case "1":
                    new ContaCorrente();
                    break;
                case "2":
                    new ContaPoupanca();
                    break;
                case "3":
                    new ContaInvestimento();
                default:
                    System.out.print("\nNão entendi o que você quis dizer com isso.");
            }
        }
    }

    public void iniciarAtendimento() {
        RelatoriosTransacoes reltransacoes = new RelatoriosTransacoes();
        String operacao = "";
        while(operacao!="q"||operacao!="Q") {
        System.out.print("\nBem-vindo! O que você deseja fazer ?\n1 - Criar outra conta\n2 - Saque" +
                        "\n3 - Depósito\n4 - Tranferência\n5 - Ver saldo\n6 - Extrato\n7 - Alterar Dados" +
                        "\n8 - Listar contas\n9 - Total do valor investido\n10 - Histórico de transações" +
                        "\n11 - Listar contas com saldo negativo"+
                        "\n");
        if(this.getClass() == ContaPoupanca.class) {
            System.out.print("12 - Simular Rendimento da poupança\nq - sair\n--> ");
        } else if (this.getClass() == ContaInvestimento.class) {
            System.out.print("12 - Simular investimento\nq - sair\n--> ");
        }else {
            System.out.print("--> ");
        }
        operacao = entrada.nextLine();
                switch(operacao) {
                    case "1":
                        criarConta();
                        break;
                    case "2":
                        if(this.getClass() == ContaCorrente.class) {
                            saqueCorrente();
                        }else{
                            saque();
                        }
                        break;
                    case "3":
                        deposito();
                        break;
                    case "4":
                        transferir();
                        break;
                    case "5":
                        saldo();
                        break;
                    case "6":
                        extrato();
                        break;
                    case "7":
                        alterarDados();
                        break;
                    case"8":
                        reltransacoes.listarContas();
                        break;
                    case "9":
                        reltransacoes.totalValorInvestido();
                        break;
                    case "10":
                        reltransacoes.historicoTransacoes();
                        break;
                    case "11":
                        reltransacoes.contasNegativo();
                        break;
                    case "q": case"Q":
                        System.out.print("\nVolte sempre! :)");
                        return;
                    default:
                        System.out.print("\nNão entendi o que você quis dizer com isso.");
                        break;
                }
            }
    }
}
