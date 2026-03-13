import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.UUID

// 1. Data Class: Demonstra conhecimento em modelos imutáveis e limpos
data class CryptoAsset(
    val id: String = UUID.randomUUID().toString(),
    val symbol: String,
    val name: String,
    val priceUsd: Double,
    val changePercent24Hr: Double? // Nullable para testar Null Safety
)

// 2. Extension Function: Mostra que você sabe estender funcionalidades sem herança
fun Double.formatCurrency(): String = "U$ ${"%.2f".format(this)}"

class MarketEngine {
    
    // 3. Mock Data com Listas: Uso de imutabilidade (listOf)
    private val assets = listOf(
        CryptoAsset(symbol = "BTC", name = "Bitcoin", priceUsd = 64230.50, changePercent24Hr = 1.45),
        CryptoAsset(symbol = "ETH", name = "Ethereum", priceUsd = 3450.20, changePercent24Hr = -0.30),
        CryptoAsset(symbol = "SOL", name = "Solana", priceUsd = 145.10, changePercent24Hr = null), // Caso nulo
        CryptoAsset(symbol = "DOT", name = "Polkadot", priceUsd = 7.15, changePercent24Hr = 2.10)
    )

    // 4. Flow & Coroutines: O padrão ouro para processamento assíncrono
    fun streamMarketData(): Flow<List<CryptoAsset>> = flow {
        while(true) {
            emit(assets)
            delay(3000) // Simula atualização a cada 3 segundos
        }
    }

    // 5. Processamento de Dados: Uso de filter e High-order functions
    fun getTopPerformers(minPrice: Double): List<CryptoAsset> {
        return assets.filter { it.priceUsd > minPrice }
                     .sortedByDescending { it.changePercent24Hr ?: 0.0 }
    }
}

fun main() = runBlocking {
    val engine = MarketEngine()
    println("🚀 KriptoFlow iniciado - Monitorando Mercado...\n")

    // 6. Scope Functions (apply/let): Mostra domínio da sintaxe moderna
    val topAssets = engine.getTopPerformers(100.0).apply {
        println("📊 Ativos de Alto Valor (> U$ 100):")
    }

    topAssets.forEach { asset ->
        // 7. Null Safety (Elvis Operator): Segurança contra crashes
        val trend = asset.changePercent24Hr?.let { if(it > 0) "📈" else "📉" } ?: "➖"
        
        println("${asset.symbol} | Preço: ${asset.priceUsd.formatCurrency()} | Variação: ${asset.changePercent24Hr ?: 0.0}% $trend")
    }
}
