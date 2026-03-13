import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.UUID

// Modelo de Dados
data class CryptoAsset(
    val id: String = UUID.randomUUID().toString(),
    val symbol: String,
    val name: String,
    val priceUsd: Double,
    val changePercent24Hr: Double?
)

// Funções de Extensão (Demonstra capricho com a UI/UX via código)
fun Double.formatCurrency(): String = "U$ ${"%.2f".format(this)}"

class MarketEngine {
    
    private val assets = listOf(
        CryptoAsset(symbol = "BTC", name = "Bitcoin", priceUsd = 64230.50, changePercent24Hr = 1.45),
        CryptoAsset(symbol = "ETH", name = "Ethereum", priceUsd = 3450.20, changePercent24Hr = -0.30),
        CryptoAsset(symbol = "SOL", name = "Solana", priceUsd = 145.10, changePercent24Hr = null),
        CryptoAsset(symbol = "DOT", name = "Polkadot", priceUsd = 7.15, changePercent24Hr = 2.10),
        CryptoAsset(symbol = "ADA", name = "Cardano", priceUsd = 0.45, changePercent24Hr = -1.20)
    )

    // FUNÇÃO 1: Stream de dados com Tratamento de Erros (Essencial para P&D)
    fun streamMarketData(): Flow<List<CryptoAsset>> = flow {
        try {
            while(true) {
                if (assets.isEmpty()) throw Exception("Banco de dados vazio")
                emit(assets)
                delay(3000)
            }
        } catch (e: Exception) {
            println("❌ Erro no Stream: ${e.message}")
        }
    }

    // FUNÇÃO 2: Busca por Símbolo (Uso de Scope Function 'let')
    fun findAsset(symbol: String): CryptoAsset? {
        return assets.find { it.symbol.equals(symbol, ignoreCase = true) }
    }

    // FUNÇÃO 3: Filtro de Performance (Uso de Sequences para maior desempenho)
    fun getAssetsByPerformance(isPositive: Boolean): List<CryptoAsset> {
        return assets.asSequence() // Transforma em Sequence para processar um por um (Performance!)
            .filter { (it.changePercent24Hr ?: 0.0) >= 0 == isPositive }
            .toList()
    }
}

// O MAIN agora simula um fluxo real de trabalho
fun main() = runBlocking {
    val engine = MarketEngine()
    
    println("--- 📊 KRIPTOFLOW: ENGINE DE ALTA PERFORMANCE ---")

    // Testando Busca com Null Safety
    val search = "BTC"
    engine.findAsset(search)?.let {
        println("🔍 Resultado da Busca ($search): ${it.name} custando ${it.priceUsd.formatCurrency()}")
    } ?: println("⚠️ Ativo $search não encontrado.")

    println("\n📈 Ativos em Alta:")
    engine.getAssetsByPerformance(true).forEach { 
        println("- ${it.symbol}: +${it.changePercent24Hr ?: 0.0}%")
    }

    println("\n📉 Ativos em Baixa:")
    engine.getAssetsByPerformance(false).forEach { 
        println("- ${it.symbol}: ${it.changePercent24Hr ?: 0.0}%")
    }

    println("\n📡 Iniciando Monitoramento em Tempo Real (5s)...")
    val job = launch {
        engine.streamMarketData().take(2).collect { data ->
            println("✅ Update: ${data.size} ativos monitorados.")
        }
    }
    
    job.join()
    println("\n--- FIM DA EXECUÇÃO ---")
}
