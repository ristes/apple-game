ClickCallbackManager = function() {
	this.callbacks = {};
	this.active = null;

	var self = this;

	this.registerCallback = function(name, callback) {
		if (!callback || !callback.onClick
				|| (typeof callback.onClick !== 'function')) {
			throw new Error("callback with function onClick must be providen!");
		}
		this.callbacks[name] = callback;

		if (this.visualizer && this.visualizer.registerAction) {
			this.visualizer.registerAction(name, self);
		}

	}

	this.activate = function(name) {
		if (!this.callbacks[name]) {
			throw new Error('Invalid callback name: ' + name);
		}
		this.active = this.callbacks[name];
	}

	this.deactivate = function() {
		this.active = null;
	}

	this.onClick = function(e) {
		if (self.active) {
			self.active.onClick(e, this);
		}
	}
}

OnClickCallbackManager = new ClickCallbackManager();

ToolbarBuilder = function(x, y, z) {
	this.x = x;
	this.y = y;
	this.z = z;
	this.k = 0;
	this.next = function() {
		var n = {
			x : (this.x + (this.k + 1) * 29 + this.k * 12),
			y : this.y + 17,
			z : this.z + 10,
			w : 12,
			h : 23
		};
		this.k++;
		return n;
	}
}

var operations = [ {
	name : 'plant',
	sprite : {
		url : "/public/images/game/sadnica.png",
		width : 70,
		height : 128
	},
	ratio : {
		w : 1 / 4,
		h : 1 / 2
	},
	action : 'AddOrRelpacePlantCallback'
}, {
	name : 'youngPlant',
	sprite : {
		url : "/public/images/game/mala-sadnica.png",
		width : 351,
		height : 545
	},
	ratio : {
		w : 1,
		h : 1
	},
	action : 'AddOrRelpacePlantCallback'
}, {
	name : 'bigPlant',
	sprite : {
		url : "/public/images/game/golema-sadnica.png",
		width : 543,
		height : 720
	},
	ratio : {
		w : 1,
		h : 3 * 1 / 2
	},
	action : 'AddOrRelpacePlantCallback'
}, {
	name : 'applePlant',
	sprite : {
		url : "/public/images/game/rodna-sadnica.png",
		width : 802,
		height : 941
	},
	ratio : {
		w : 1,
		h : 3 * 1 / 2
	},
	action : 'AddOrRelpacePlantCallback'
}, {
	name : 'prskanje',
	sprite : {
		url : "/public/images/game/prskanje.png",
		width : 128,
		height : 128
	},
	ratio : {
		w : 1,
		h : 1
	},
	action : 'AddEffectOnTop'
} ];

OperationsPanelComponent = function(x, y, z, atom, columns) {
	var toolbarBuilder = new ToolbarBuilder(x, y, z);

	Crafty.sprite(2287, 265, "/public/images/game/dolen-element.png", {
		itemsPanel : [ 0, 0, 1, 1 ]
	});

	this.panel = Crafty.e("2D, Canvas, itemsPanel, Mouse").attr({
		x : x,
		y : y,
		z : z,
		w : 570,
		h : 64
	});

	this.panel.buttons = [];

	for (var j = 0; j < operations.length; j++) {
		var newPlant = new OperationComponent(operations[j], toolbarBuilder
				.next(), atom, columns);

		this.panel.buttons.push(newPlant.element);
	}

}

OperationComponent = function(oper, toolbar, atom, columns) {
	var sp = {};
	sp[oper.name] = [ 0, 0 ];

	Crafty.sprite(oper.sprite.width, oper.sprite.height, oper.sprite.url, sp);

	var callback = window[oper.action];
	OnClickCallbackManager.registerCallback(oper.name, new callback(oper.name,
			oper.ratio, atom, columns));

	this.element = Crafty.e("2D, Canvas, " + oper.name + ", Mouse").attr({
		x : toolbar.x,
		y : toolbar.y,
		z : toolbar.z,
		w : toolbar.w,
		h : toolbar.h
	}).bind('Click', function(e) {
		OnClickCallbackManager.activate(oper.name);
	});
}

AddOrRelpacePlantCallback = function(name, ratio, atom, zLevles) {

	function createPlant(atom) {
		return Crafty.e("2D, Canvas, " + name).attr({
			w : ratio.w * atom,
			h : ratio.h * atom
		});
	}

	this.onClick = function(e, landPart) {
		if (landPart.plant) {
			landPart.plant.destroy();
		}

		var plant = createPlant(atom);
		plant.ratio = ratio;
		plant.attr({
			x : landPart._x + (atom - ratio.w * atom) / 2,
			y : landPart._y + (atom / 2 - ratio.h * atom) - atom / 4,
			z : landPart._z + zLevles
		});

		landPart.plant = plant;
	};
}

AddEffectOnTop = function(name, ratio, atom, zLevles) {

	function createEffect(atom) {
		return Crafty.e("2D, DOM, " + name + ", SpriteAnimation").reel(name,
				30, 0, 0, 3).animate(name, 30).bind("EnterFrame", function() {
			if (!this.isPlaying()) {
				this.destroy();
			}
		});
		;
	}

	this.onClick = function(e, landPart) {
		if (!landPart.plant) {
			return;
		}

		var eff = createEffect(atom);
		eff.attr({
			x : landPart.plant._x,
			y : landPart.plant._y,
			z : landPart.plant._z + zLevles,
			w : landPart.plant._w,
			h : landPart.plant._h

		});
	}

}