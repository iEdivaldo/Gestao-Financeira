import { Component, OnInit, signal } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { HttpClient } from '@angular/common/http';

// Interface local para o resumo do dashboard
interface ResumoDashboard {
  saldoTotal: number;
  totalReceitas: number;
  totalDespesas: number;
  totalPendente: number;
}

// Interface para transações recentes
interface TransacaoRecente {
  id: string;
  descricao: string;
  valor: number;
  tipo: 'RECEITA' | 'DESPESA';
  status: string;
  dataVencimento: string;
  categoriaNome: string;
  categoriaIcone: string;
  categoriaCor: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule, RouterModule,
    MatCardModule, MatIconModule, MatButtonModule,
    MatTableModule, MatChipsModule, MatProgressBarModule,
    BaseChartDirective
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  // Signals de estado
  carregando = signal(true);
  resumo = signal<ResumoDashboard>({
    saldoTotal: 0,
    totalReceitas: 0,
    totalDespesas: 0,
    totalPendente: 0
  });
  transacoesRecentes = signal<TransacaoRecente[]>([]);

  // Colunas da tabela de transações recentes
  colunasTabela = ['data', 'descricao', 'categoria', 'valor', 'status'];

  // Mês e ano atual para os filtros
  readonly mesAtual = new Date().getMonth() + 1;
  readonly anoAtual = new Date().getFullYear();

  // ── Dados do gráfico de pizza (despesas por categoria) ────────────────────
  graficoPizzaDados: ChartData<'doughnut'> = {
    labels: [],
    datasets: [{
      data: [],
      backgroundColor: [],
      borderWidth: 2,
      borderColor: '#fff'
    }]
  };

  graficoPizzaOpcoes: ChartOptions<'doughnut'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { position: 'right' },
      tooltip: {
        callbacks: {
          // Formata o tooltip como moeda brasileira
          label: (ctx) => ` R$ ${ctx.parsed.toFixed(2).replace('.', ',')}`
        }
      }
    }
  };

  // ── Dados do gráfico de barras (receitas vs despesas 6 meses) ─────────────
  graficoBarrasDados: ChartData<'bar'> = {
    labels: [], // nomes dos últimos 6 meses
    datasets: [
      {
        label: 'Receitas',
        data: [],
        backgroundColor: 'rgba(67, 160, 71, 0.8)',
        borderRadius: 6
      },
      {
        label: 'Despesas',
        data: [],
        backgroundColor: 'rgba(229, 57, 53, 0.8)',
        borderRadius: 6
      }
    ]
  };

  graficoBarrasOpcoes: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { position: 'top' }
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          // Formata o eixo Y como valor monetário compacto
          callback: (value) => `R$ ${Number(value).toLocaleString('pt-BR')}`
        }
      }
    }
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.carregarDashboard();
  }

  private carregarDashboard(): void {
    // Chama o endpoint de relatório mensal para obter os dados do dashboard
    this.http.get<any>(
      `/api/v1/relatorios/mensal?mes=${this.mesAtual}&ano=${this.anoAtual}`
    ).subscribe({
      next: (dados) => {
        // Atualiza o resumo com os dados do backend
        this.resumo.set({
          saldoTotal: dados.saldo,
          totalReceitas: dados.totalReceitas,
          totalDespesas: dados.totalDespesas,
          totalPendente: dados.totalPendente
        });

        // Atualiza o gráfico de pizza com despesas por categoria
        this.graficoPizzaDados = {
          labels: dados.despesasPorCategoria.map((c: any) => c.categoriaNome),
          datasets: [{
            data: dados.despesasPorCategoria.map((c: any) => c.total),
            backgroundColor: dados.despesasPorCategoria.map((c: any) => c.categoriaCor),
            borderWidth: 2,
            borderColor: '#fff'
          }]
        };

        this.carregando.set(false);
      },
      error: () => this.carregando.set(false)
    });

    // Carrega as transações recentes
    this.http.get<any>(
      `/api/v1/transacoes?mes=${this.mesAtual}&ano=${this.anoAtual}&size=10`
    ).subscribe({
      next: (dados) => {
        // A resposta pode ser paginada — pega o content se for Page do Spring
        this.transacoesRecentes.set(dados.content ?? dados);
      }
    });
  }

  // Retorna a classe CSS baseada no tipo da transação
  classeTipo(tipo: string): string {
    return tipo === 'RECEITA' ? 'valor-receita' : 'valor-despesa';
  }

  // Retorna a classe CSS baseada no status
  classeStatus(status: string): string {
    const mapa: Record<string, string> = {
      PAGO: 'status-pago',
      RECEBIDO: 'status-recebido',
      PENDENTE: 'status-pendente',
      CANCELADO: 'status-cancelado'
    };
    return mapa[status] ?? '';
  }

  // Label amigável para o status
  labelStatus(status: string): string {
    const mapa: Record<string, string> = {
      PAGO: 'Pago',
      RECEBIDO: 'Recebido',
      PENDENTE: 'Pendente',
      CANCELADO: 'Cancelado'
    };
    return mapa[status] ?? status;
  }
}