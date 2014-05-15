'use strict';

Game.controller('LoginController', ['$scope', '$translate',
    function($scope, $translate) {

    }]);

Game.controller('StoreController', ['$scope', '$translate', '$http', 'Store',
    function($scope, $translate, $http, Store) {

      $scope.initStore = function(store, nextState, servMethod) {
        $scope.nextState = nextState;
        $scope.servMethod = servMethod || 'buy';
        console.log(store);

        // load from service
        $scope.items = [{
          id: 1,
          name: 'riste',
          url: '/public/images/game/plant.png'
        }, {
          id: 2,
          name: 'koki',
          url: '/public/images/game/plant.png'
        }, {
          id: 3,
          name: 'daci',
          url: '/public/images/game/plant.png'
        }];

        $scope.$root.$emit('shop-show', {
          items: $scope.items,
          showNext: false,
          storeUrl: '/public/images/home/prodavnica-icon-gore.png'
        });
      };

      $scope.onBuyItem = function(_scope, item) {
        Store[$scope.servMethod]({
          itemid: item.id,
          quantity: 1,
          currentState: "/" + $scope.nextState
        }, null, function(result) {
          console.log(result)
          if (result) {
            $scope.$root.farmer = result;
            $scope.$root.$emit('shop-hide');
          } else {
            alert("NEMA PARI!");
          }
        }).$promise['finally'](function() {
          $scope.$root.$emit('item-bought');
        });
      };

      var unreg = $scope.$root.$on('buy-item', $scope.onBuyItem);

      $scope.$on("$destroy", function() {
        unreg();
      })

    }]);

Game.controller('PlantingController', ['$scope', '$translate',
    function($scope, $translate) {

    }]);

Game.controller('UserInfoController', ['$scope', '$translate', '$http',
    '$location', function($scope, $translate, $http, $location) {

      $.post("/AuthController/farmer", function(data) {
        $scope.$root.farmer = data;
        $scope.farmer = data;
        $scope.username = data.username;
        $scope.balans = data.balans;

        $scope.$root.$watch('farmer', function(n, o) {
          if (o === n || n === null) return;
          $location.path(n.currentState);
        });
        if (data.currentState === null) {
          $location.path("/buy_tractor");
        } else {
          $location.path("/buy_tractor");
          // $location.path(data.currentState);
        }

      });

    }]);
Game.controller('WeatherController', ['$scope', '$translate', 'Crafty',
    'ModelStore', 'jQuery',
    function($scope, $translate, Crafty, ModelStore, $) {
      $.post("/WeatherController/weatherforecast?fordays=3", function(data) {
        $scope.weather = data;
      });
    }]);
Game.controller('ShopController', ['$scope', '$translate', 'Crafty',
    'ModelStore', 'jQuery',
    function($scope, $translate, Crafty, ModelStore, $) {
      $.post("/storecontroller/allNg", function(data) {
        $scope.main_store = data;
      });
    }]);
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
