Crafty.c("CompositeDataComponent", {
	init : function() {
		this.childCmps = [];
		var self = this;
		this.bind("Remove", function() {
			for (var i = 0; i < self.childCmps.length; i++) {
				self.childCmps[i].destroy();
			}
		});

	},
	addCmp : function(cmp) {
		this.childCmps = this.childCmps || [];
		var self = this;

		cmp.bind("Remove", function() {
			self.childCmps.splice($.inArray(cmp, self.childCmps), 1);
		});
		this.childCmps.push(cmp);
		cmp.container = this;
		if (this.x) {
			cmp.attr("x", this.x + cmp.xoffset);
		}
		if (this.y) {
			cmp.attr("y", this.y + cmp.yoffset);
		}
	},
	data : function(data) {
		this.childCmps = this.childCmps || [];
		this.display(data);
		this.modelData = data;
		for (var i = 0; i < this.childCmps.length; i++) {
			if (this.childCmps[i].data
					&& typeof this.childCmps[i].data === "function") {
				this.childCmps[i].data(data);
			}
		}

		var self = this;
		this.bind("Change", function(obj) {
			if (obj.hasOwnProperty("x")) {
				for (var i = 0; i < self.childCmps.length; i++) {
					var cmp = self.childCmps[i];
					if (cmp.xoffset) {
						cmp.attr("x", obj.x + cmp.xoffset);
					}
				}
			}
			if (obj.hasOwnProperty("y")) {
				for (var i = 0; i < self.childCmps.length; i++) {
					var cmp = self.childCmps[i];
					if (cmp.yoffset) {
						cmp.attr("y", obj.y + cmp.yoffset);
					}
				}
			}
		});

		return this;
	},
	display : function(data) {
		this.text(data.description);
	}
});