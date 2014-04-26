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
<<<<<<< HEAD
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
=======
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
			}).bind("Click", function() {
				var features = this.terrain.analysis.features;
				for (var i = 0; i < features.length; i++) {
					console.log(features[i]);
				}
				ModelStore.getFromService("/terrainshop/analyze?terrainId=" + this.terrain.analysis.entityId, function(dataTerrain) {
					for (var j = 0; j < dataTerrain.length; j++) {
						var analyseFeatures = Crafty.e("2D, DOM, Text, Mouse").attr({
							w : 400,
							h : 20,
							x : 600,
							y : 120 + 30 * j
						}).text(dataTerrain[j].analysis + dataTerrain[j].value).css({
							"text-align" : "left",
							"font-size" : "50px",
							"padding-top" : "5px",
							"padding-left" : "5px",
							"background-color" : "green"
						}).bind("Click", function() {
							console.log(this);
						});
					}
				});
>>>>>>> 99b6bf559b7c135d50e19057f876daebe857a94d
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
<<<<<<< HEAD

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
=======
});
>>>>>>> 99b6bf559b7c135d50e19057f876daebe857a94d
