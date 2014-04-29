Crafty.c("UserLabel", {
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
	},
	data : function(data) {
		this.text(this.t);
		return this;
	}
});
Crafty.c("UserFrame", {
	init : function() {
		this.childCmps = [];
		this.balanceComponent
		this.addComponent("2D, DOM, Text, Mouse, CompositeDataComponent");
		this.attr({
			w : 200,
			h : 30
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
			this.text(data.username).textFont({
				size : "20px"
			});
			var datefarmer = Crafty.e("UserLabel").attr({
				xoffset : 1,
				yoffset : 25
			}).textFont({
				size : "20px"
			}).setText(data.gameDate.date);
			var balancefarmer = Crafty.e("UserLabel").attr({
				xoffset : 1,
				yoffset : 50
			}).textFont({
				size : "20px"
			}).setText("$" + data.balans);
			//this.childCmps.push(namefarmer);
			this.addCmp(balancefarmer);
			this.addCmp(datefarmer);
			this.childCmps.push(balancefarmer);
			this.childCmps.push(datefarmer);
			this.balanceComponent = balancefarmer;
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

