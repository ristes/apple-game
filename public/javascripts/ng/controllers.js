'use strict';

Game.controller('UserInfoController', ['$scope', '$translate', 'Crafty',
    'ModelStore', 'jQuery',
    function($scope, $translate, Crafty, ModelStore, $) {

      $scope.itemClick = function(a) {
        a.action();
      }

      $scope.actions = [{
        ico: "prodavnica_home_icon",
        order: 1,
        action: function() {
          $scope.$root.$emit('shop-show', {
            id: 'prodavnica_home_icon'
          })
        }
      }, {
        ico: "korisnik_home_icon",
        order: 2,
        action: function() {

        }
      }, {
        ico: "akcii_home_icon",
        order: 3,
        action: function() {

        }
      }, {
        ico: "analiza_home_icon",
        order: 4,
        action: function() {

        }
      }];

      $.post("/AuthController/farmer", function(data) {
        $scope.$root.farmer = data;
        $scope.farmer = data;
        $scope.username = data.username;
        $scope.balans = data.balans;
      });
      
      $.get("/WeatherController/weatherforecast?fordays=5",function(data) {
			$scope.$root.weather = data;
		});
		
    }]);
Game.controller('WeatherController', [
	'$scope',
	'$translate',
	'Crafty',
	'ModelStore',
	'jQuery',
	function($scope,$translate,Crafty,ModelStore,$) {
		$.post("/WeatherController/weatherforecast?fordays=3",function(data) {
			$scope.weather = data;
		});
	}]);
Game.controller('ShopController',[
	'$scope',
	'$translate',
	'Crafty',
	'ModelStore',
	'jQuery',
	function($scope,$translate,Crafty,ModelStore,$) {
		$.post("/storecontroller/allNg",function(data) {
			$scope.main_store = data;
		});
	}
]);
Game.controller('PlantationController', [
    '$scope',
    '$translate',
    'Crafty',
    'ModelStore',
    function($scope, $translate, Crafty, ModelStore) {
      Crafty.init(1000, 500, "target");

      Crafty.scene("loading", function() {

        // black background with some loading text
        Crafty.e("2D, DOM, Text").attr({
          w: 100,
          h: 20,
          x: 150,
          y: 120
        }).text("Loading scenes").css({
          "text-align": "center",
          "font-size": "50px"
        });
      });

      // automatically play the loading scene
      Crafty.scene("loading");
      var images = ["/public/images/sky.png",
          "/public/images/game/bg_grass.png", "/public/images/sprite.png",
          "/public/images/game/dolen-element.png"];

      ModelStore.getAll("models.SpriteImage", function(sprites) {
        for (var j = 0; j < sprites.length; j++) {
          images.push(sprites[j].url);

          var sp = {};
          sp[sprites[j].name] = [0, 0];
          Crafty
                  .sprite(sprites[j].width, sprites[j].height, sprites[j].url,
                          sp);
        }
        Crafty.load(images, function() {
          $.post("/AuthController/farmer", function(data) {
            AppleGame.changeState(data);
            $.post("/weathercontroller/weatherforecast?fordays=3", function(
                    data) {
              AppleGame.setWeather(data);
              if (AppleGame.farmer.currentState) {
                Crafty.scene(AppleGame.farmer.currentState);
              } else {
                Crafty.scene("plantation");
              }
            });

          });
        });
      });
    }]);
