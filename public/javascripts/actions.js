var PlantMouse = true;
ActionCallback = function() {
	var self = this;

	this.initSprites = function(operation) {
		self.defineSprite(operation.name, operation.icon);
		self.defineSprite(operation.name + "_plant", operation.plant);
		self.defineSprite(operation.name + "_effect", operation.effect);
		self.defineSprite(operation.name + "_ground", operation.ground);
	}

	this.createPlant = function(operation, size, cmp, z) {
		if (cmp.plant) {
			cmp.plant.destroy();
		}
		var plant = self.createElement(operation, size, operation.name
				+ "_plant", operation.plant);

		plant.attr({
			x : cmp._x + (size - plant.def.ratio.w * size) / 2,
			y : cmp._y + (size / 2 - plant.def.ratio.h * size) - size / 4,
			z : cmp._z + z
		});

		if (PlantMouse) {
			plant.bind("MouseOver", function() {
				plant.ground.def.over.apply(plant.ground);
			}).bind("MouseOut", function() {
				plant.ground.def.out.apply(plant.ground);
			});
		} else {
			plant.removeComponent("Mouse");
		}

		plant.ground = cmp;
		cmp.plant = plant;
		return plant;

	}

	this.createEffect = function(operation, size, cmp, z) {
		if (cmp.plant) {

			var name = operation.name + "_effect";
			var effect = self.createElement(operation, size, name,
					operation.effect);
			effect.attr({
				x : cmp.plant._x,
				y : cmp.plant._y,
				z : cmp.plant._z + z,
				w : cmp.plant._w,
				h : cmp.plant._h

			});
			effect.reel(name, operation.effect.speed, 0, 0,
					operation.effect.frameNumber);
			effect.animate(name, operation.effect.duration);
			if (operation.effect.shouldDestroy) {
				effect.bind("EnterFrame", function() {
					if (!this.isPlaying()) {
						this.destroy();
					}
				});
			}
			return effect;
		}
	}

	this.createGround = function(operation, size, cmp, z) {
		var ground = self.createElement(operation, size, operation.name
				+ "_ground", operation.ground);
		ground.attr({
			x : cmp._x,
			y : cmp._y,
			z : cmp._z,
			w : cmp._w,
			h : cmp._h
		});
		ground.bind("MouseOver", cmp.def.over).bind("MouseOut", cmp.def.out)
				.bind("Click", OnClickCallbackManager.onClick);

		if (cmp.plant) {
			ground.plant = cmp.plant;
			ground.plant.ground = ground;
		}

		ground.def.i = cmp.def.i;
		ground.def.j = cmp.def.j
		ground.def.out = cmp.def.out;
		ground.def.over = cmp.def.over;
		ground.def.field = cmp.def.field;
		ground.def.field[ground.def.i + "-" + ground.def.j] = ground;
		cmp.destroy();
		return ground;
	}

	this.createElement = function(operation, size, name, sprite) {
		if (sprite) {
			var element = Crafty.e(
					"2D, Canvas, SpriteAnimation, Mouse, " + name).attr({
				w : sprite.ratio.w * size,
				h : sprite.ratio.h * size
			});
			if (sprite.areaMap) {
				var am = [];
				for ( var i in sprite.areaMap) {
					am.push([ sprite.areaMap[i][0] * size,
							sprite.areaMap[i][1] * size ]);
				}
				element.areaMap(new Crafty.polygon(am));
			}
			element.def = sprite;
			// element.sprite = Crafty.sprite;
			return element;
		}
	}

	this.defineSprite = function(name, sprite) {
		if (sprite) {
			var sp = {};
			sp[name] = [ 0, 0 ];

			Crafty.sprite(sprite.width, sprite.height, sprite.url, sp);
		}
	}

}

ActionCallback.extend = function(constructor) {

	constructor.prototype = new ActionCallback();
	constructor.prototype.constructor = constructor;
	return constructor;
}

AddEffectOnTop = ActionCallback.extend(function(operation, atom, zLevles) {

	var self = this;
	this.size = atom;
	this.operation = operation;

	this.initSprites(operation);

	this.onClick = function(e, landPart) {
		if (!landPart.plant) {
			return;
		}

		var eff = self.createEffect(self.operation, atom, landPart, zLevles);

		var newPlant = self
				.createPlant(self.operation, atom, landPart, zLevles);

		var newLand = self
				.createGround(self.operation, atom, landPart, zLevles);
	}

});

ProgressCallback = ActionCallback.extend(function(operation, atom, zLevles) {

	var self = this;
	this.size = atom;
	this.operation = operation;

	this.initSprites(operation);

	Crafty.sprite(22, 18, "/public/images/game/progress-full.png", {
		progress : [ 0, 0 ]
	});

	function createPointer(atom) {
		return Crafty.e("2D, Canvas, Tween, " + self.operation.name);
	}

	function createProgress() {
		return Crafty.e("2D, Canvas, Tween, progress");
	}

	this.onClick = function(e, landPart) {
	}

	this.onActivate = function(field) {

		var activated = true;
		var xstart = 650;
		var ystart = 40;
		var interval = 10;
		var pointer = createPointer(atom);

		pointer.attr({
			x : xstart,
			y : ystart,
			z : 10000,
			w : self.operation.icon.ratio.w * atom,
			h : self.operation.icon.ratio.h * atom,
			alpha : 0.0
		}).tween({
			alpha : 1.0
		}, 3000).bind('TweenEnd', function() {
			if (activated)
				interval = setInterval(showProgress, self.operation.duration);
			activated = false;

		});
		var prog = createProgress();
		var offset = self.operation.icon.ratio.w * atom / 2;
		var progw = prog._w;
		prog.attr({
			x : xstart + offset,
			y : ystart + self.operation.icon.ratio.h * atom,
			z : 10000
		});

		var progres = [];
		progres.push(prog);

		var keys = [];
		for ( var v in field.field) {
			if (typeof v === 'string') {
				keys.push(v);
			}
		}
		keys.reverse();
		var total = keys.length + 1;
		function showProgress() {
			pointer.attr({
				x : pointer.x + progw
			});
			prog = createProgress();
			prog.attr({
				x : pointer._x + offset,
				y : ystart + self.operation.icon.ratio.h * atom,
				z : 10000
			});
			var landPart = field.field[keys.pop()];
			if (landPart) {
				if (landPart.plant) {
					self.createEffect(self.operation, atom, landPart, zLevles);
					self.createPlant(self.operation, atom, landPart, zLevles);
				}
				self.createGround(self.operation, atom, landPart, zLevles);
			}
			landPart = field.field[keys.pop()];
			if (landPart) {
				if (landPart.plant) {
					self.createEffect(self.operation, atom, landPart, zLevles);
					self.createPlant(self.operation, atom, landPart, zLevles);
				}
				self.createGround(self.operation, atom, landPart, zLevles);
			}

			progres.push(prog);
			total--;
			total--;
			if (total <= 0) {
				finish(pointer, progres);
			}
		}

		function finish(pointer, progres) {
			clearInterval(interval);
			pointer.tween({
				alpha : 0.0
			}, 4000).bind("TweenEnd", function() {
				this.destroy();
			});
			for (var t = 0; t < progres.length; t++) {
				progres[t].tween({
					alpha : 0.0
				}, 4000).bind("TweenEnd", function() {
					this.destroy();
				});
			}
		}

	}
});
