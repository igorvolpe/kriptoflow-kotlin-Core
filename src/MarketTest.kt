// Simulação de Teste Unitário (Manual para portfólio)
fun testMarketCalculations() {
    val engine = MarketEngine()
    val btc = engine.findAsset("BTC")
    
    // Asserção lógica
    if (btc?.symbol == "BTC") {
        println("✅ Teste Unitário: findAsset - PASSOU")
    } else {
        println("❌ Teste Unitário: findAsset - FALHOU")
    }
}
