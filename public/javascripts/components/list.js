Crafty.c("List", {
	init : function() {
		this.listItems = [];
		var self = this;
		this.bind("Remove", function() {
			for (var i = 0; i < self.listItems.length; i++) {
				self.listItems[i].destroy();
			}
		});
	},
	fx : function(item, x, y, padding) {
		return x;
	},
	fy : function(item, x, y, padding) {
		return y + item.order * (item.h + padding);
	},
	itemFn : function(fn) {
		this.itemConstructor = fn;
		return this;
	},
	load : function(url, x, y, padding, onloadCallback) {
		var self = this;
		ModelStore.getFromService(url, function(data) {
			onloadCallback.apply(self);
			function heightChanged(obj) {
				if (!obj.hasOwnProperty("h")) {
					return;
				}
				var ny = this.y + this.h + padding;
				for (var j = this.order + 1; j < self.listItems.length; j++) {
					var ni = self.listItems[j].attr("y", ny);
					ny = ni.y + ni.h + padding;
				}

			}
			
			if (!Array.isArray(data)) {
				var item = self.itemConstructor(data, x, y);
				item.order = 0;
				item.attr({
					x : self.fx(item, x, y, padding),
					y : self.fy(item, x, y, padding)
				});
				item.bind("Change", heightChanged);

				self.listItems.push(item);
			}
			
			for (var i = 0; i < data.length; i++) {
				var item = self.itemConstructor(data[i], x, y);
				item.order = i;
				item.attr({
					x : self.fx(item, x, y, padding),
					y : self.fy(item, x, y, padding)
				});
				item.bind("Change", heightChanged);

				self.listItems.push(item);
			}
		});
		return this;
	}
});