Crafty.c("WeatherLabel", {
	init : function() {
		this.childCmps = [];
		this.addComponent("2D, DOM, Text, Mouse");

		this.attr({
			w : 20,
			h : 15
		});
		this.css({
			"text-align" : "center",
			"font-size" : "x-large",
			"padding-top" : "5px",
		});
		this.bind("Invalidate", function(data) {
			if (data != undefined) {
				this.text("$"+data.balans);
			}
		});

	},
	setText : function(t) {
		this.t = t;
		return this;
	}
});
Crafty.c("WeatherFrame", {
	init : function() {
		this.childCmps = [];
		this.addComponent("2D, DOM, Text, Mouse, CompositeDataComponent");
		this.attr({
			w : 80,
			h : 200
		});
		this.css({
			"text-align" : "left",
			"font-size" : "50px",
			"padding-top" : "5px",
			"padding-left" : "5px",
			"cursor" : "pointer",
		});
		self = this;
		this.display = function(data) {
			console.log(data);
			icon = Crafty.e("2D, DOM, " + data.icon_url).attr({
				xoffset : 10,
				yoffset : 25,
				w : 45,
				h : 70
			});
			this.text(data.date).textFont({
				size : "12px"
			});
		
			var lowTemp = Crafty.e("WeatherLabel").attr({
				xoffset : 20,
				yoffset : 100
			}).textFont({
				size : "16px"
			}).text(parseInt(data.lowTemp).toString()+'C');
			var highTemp = Crafty.e("WeatherLabel").attr({
				xoffset : 20,
				yoffset : 120
			}).textFont({
				size : "16px"
			}).text(parseInt(data.highTemp)+"C");
			this.addCmp(icon);
			this.addCmp(lowTemp);
			this.addCmp(highTemp);
			this.childCmps.push(lowTemp);
			this.childCmps.push(highTemp);
		};
		this.bind("Invalidate", function(data) {
			//this.text(data);
			if (this.balanceComponent !== undefined) {
				this.balanceComponent.trigger("Invalidate", data);
			}
		});
		// this.bind("Click", function() {
		// $.post("/terrainshop/buyBase?baseId=" + this.modelData.entityId, function() {
		// Crafty.scene("seedlingShop");
		// });
		// });

	}
});