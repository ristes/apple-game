Crafty.scene("terrainShop", function() {
	ModelStore.getFromService("/terrainshop/allTerrains", function(data) {
		for (var i = 0; i < data.length; i++) {
			var btn = Crafty.e("2D, DOM, Text, Mouse").attr({
				w : 400,
				h : 20,
				x : 150,
				y : 120 + 30 * i
			}).text(data[i].description).css({
				"text-align" : "left",
				"font-size" : "50px",
				"padding-top" : "5px",
				"padding-left" : "5px",
				"background-color" : "green"
			});
			var analyze = Crafty.e("2D, DOM, Text, Mouse").attr({
				w : 40,
				h : 14,
				x : 250,
				y : 123 + 30 * i
			}).text("analyze").css({
				"text-align" : "center",
				"font-size" : "50px",
				"padding-top" : "5px",
				"cursor" : "pointer",
				"background-color" : "red"
			}).bind(
					"Click",
					function() {
						ModelStore.getFromService(
								"/terrainshop/analyze?terrainId="
										+ this.terrain.entityId, function(data) {

								});
					});
			analyze.terrain = data[i];

			var buy = Crafty.e("2D, DOM, Text, Mouse").attr({
				w : 30,
				h : 14,
				x : 300,
				y : 123 + 30 * i
			}).text("buy").css({
				"text-align" : "center",
				"font-size" : "50px",
				"padding-top" : "5px",
				"cursor" : "pointer",
				"background-color" : "red"
			}).bind("Click", function() {
				console.log("buy: " + this.terrain.description);
			});
			buy.terrain = data[i];
		}
	});
});