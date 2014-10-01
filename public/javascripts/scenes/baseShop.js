Crafty.c("BaseText", {
	init : function() {
		this.childCmps = [];
		this.addComponent("2D, DOM, Text, Mouse, CompositeDataComponent");
		this.attr({
			w : 100,
			h : 20
		});
		this.css({
			"text-align" : "left",
			"font-size" : "50px",
			"padding-top" : "5px",
			"padding-left" : "5px",
			"cursor" : "pointer",
			"background-color" : "green"
		});
		this.display = function(data) {
			this.text(data.name + " - $" + data.price);
		}
		this.bind("Click", function() {
			var msg = "Are you sure that you want to by this base?";
			AppleGame.buy("/terrainshop/buyBase?baseId="
					+ this.modelData.entityId, this.modelData.price, msg);
		});
	}
});

Crafty.scene("baseShop", function() {
	loading = Crafty.e("2D, DOM, Text").attr({
		w : 100,
		h : 20,
		x : 150,
		y : 40
	}).text("Loading Bases").css({
		"text-align" : "center",
		"font-size" : "50px"
	});

	Crafty.e("List").itemFn(function(data, x, y) {
		var item = Crafty.e("BaseText").data(data).attr({
			x : x,
			y : y
		});

		return item;
	}).load("/terrainshop/allBases", 150, 30, 10, function() {
		var userframe = Crafty.e("UserFrame").data(AppleGame.farmer).attr({
			x : 800,
			y : 30
		});
		loading.destroy();
	});

});