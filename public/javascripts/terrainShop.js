Crafty.c("TerrainFeature", {
	init : function() {
		this.addComponent("2D, DOM, Text, Mouse");
		this.attr({
			w : 285,
			h : 50
		});
		this.css({
			"text-align" : "center",
			"font-size" : "50px",
			"padding-top" : "5px",
			"background-color" : "grey"
		});
	},
	data : function(data) {
		var text = "";
		for (var i = 0; i < data.length; i++) {
			text += data[i].category.name + ": " + data[i].value + "; ";
		}
		this.text(text);
		this.features = data;
		return this;
	}
});

Crafty.c("AnalyzeTerrainButton", {
	init : function() {
		this.addComponent("2D, DOM, Text, Mouse");
		this.attr({
			w : 40,
			h : 14
		});
		this.css({
			"text-align" : "center",
			"font-size" : "50px",
			"padding-top" : "5px",
			"cursor" : "pointer",
			"background-color" : "red"
		});
		this.bind("Click", this.analyze);
		this.text("analyze");
	},
	data : function(data) {
		this.modelData = data;
		return this;
	},
	analyze : function() {
		var self = this;
		ModelStore.getFromService("/terrainshop/analyze?terrainId="
				+ this.modelData.entityId, function(data) {

			var cmp = Crafty.e("TerrainFeature").data(
					self.modelData.analysis.features).attr({
				xoffset : 10,
				yoffset : 50
			});
			self.container.addCmp(cmp);
			self.container.attr("h", self.container.h + cmp.h + 16);
			self.destroy();
		});
	}
});

Crafty.c("BuyTerrainButton", {
	init : function() {
		this.addComponent("2D, DOM, Text, Mouse");
		this.attr({
			w : 55,
			h : 14
		});
		this.css({
			"text-align" : "center",
			"font-size" : "50px",
			"padding-top" : "5px",
			"padding-left" : "0px",
			"cursor" : "pointer",
			"background-color" : "red"
		});
		this.bind("Click", this.buy);
		this.text("buy");
	},
	data : function(data) {
		this.modelData = data;
		this.text(this.size + "Ha-$"
				+ (this.size * this.modelData.analysis.unitPrice).toFixed(1));
		return this;
	},
	areaHa : function(s) {
		this.size = s;
		return this;
	},
	buy : function() {
		$.post("/terrainshop/buyTerrain?size=" + this.size + "&terrainId="
				+ this.modelData.entityId, function() {
			Crafty.scene("baseShop");
		});
	}
});

Crafty.c("TerrainText", {
	init : function() {
		this.childCmps = [];
		this.addComponent("2D, DOM, Text, Mouse, CompositeDataComponent");
		this.attr({
			w : 305,
			h : 45
		});
		this.css({
			"text-align" : "left",
			"font-size" : "50px",
			"padding-top" : "5px",
			"padding-left" : "5px",
			"background-color" : "green"
		});
		this.addCmp(Crafty.e("AnalyzeTerrainButton").attr({
			xoffset : 265,
			yoffset : 3
		}));

		this.addCmp(Crafty.e("BuyTerrainButton").areaHa(0.1).attr({
			xoffset : 5,
			yoffset : 25
		}));

		this.addCmp(Crafty.e("BuyTerrainButton").attr({
			xoffset : 65,
			yoffset : 25
		}).areaHa(0.2));

		this.addCmp(Crafty.e("BuyTerrainButton").attr({
			xoffset : 125,
			yoffset : 25
		}).areaHa(0.5));

		this.addCmp(Crafty.e("BuyTerrainButton").attr({
			xoffset : 185,
			yoffset : 25
		}).areaHa(1));

		this.addCmp(Crafty.e("BuyTerrainButton").areaHa(2).attr({
			xoffset : 250,
			yoffset : 25
		}));
	}
});

Crafty.scene("terrainShop", function() {
	loading = Crafty.e("2D, DOM, Text").attr({
		w : 100,
		h : 20,
		x : 150,
		y : 40
	}).text("Loading Terrains").css({
		"text-align" : "center",
		"font-size" : "50px"
	});

	Crafty.e("List").itemFn(function(data, x, y) {
		var item = Crafty.e("TerrainText").data(data).attr({
			x : x,
			y : y
		});

		return item;
	}).load("/terrainshop/allTerrains", 150, 30, 10, function() {
		loading.destroy();
	});

});