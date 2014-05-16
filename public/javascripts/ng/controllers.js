'use strict';

Game.controller('LoginController', ['$scope', '$translate',
    function($scope, $translate) {

    }]);

Game.controller('StoreController', ['$scope', '$translate', '$http', 'Store',
    'StoreItems', function($scope, $translate, $http, Store, StoreItems) {

      $scope.initStore = function(store, nextState, servMethod, shopIcon) {
        $scope.nextState = nextState;
        $scope.servMethod = servMethod || 'buy';

        // load from service
        $scope.items = StoreItems[store];

        $scope.$root.$emit('shop-show', {
          items: $scope.items,
          showNext: false,
          storeUrl: shopIcon
        });
      };

      $scope.onBuyItem = function(_scope, item) {
        Store[$scope.servMethod]({
          itemid: item.id,
          quantity: 1,
          currentState: "/" + $scope.nextState
        }, null, function(result) {
          if (result.balans) {
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

Game.controller('BuyTerrainController', ['$scope', '$translate', '$http',
    'Store', 'StoreItems',
    function($scope, $translate, $http, Store, StoreItems) {

      // load from service
      $scope.items = StoreItems['terrain-size'];

      $scope.$root.$emit('shop-show', {
        items: $scope.items,
        showNext: false,
        storeUrl: '/public/images/game/pocva-prodavnica-icon.png'
      });

      $scope.onSelectSize = function(_scope, item) {
        $scope.size = item.size;
        $scope.unreg();
        $scope.$root.$emit('shop-hide');
        $scope.$root.$emit('item-bought');
        $scope.items = StoreItems['terrain'];
        $scope.$root.$emit('shop-show', {
          items: $scope.items,
          showNext: false,
          storeUrl: '/public/images/game/pocva-prodavnica-icon.png'
        });
        $scope.unreg = $scope.$root.$on('buy-item', $scope.onBuyItem);

      };

      $scope.onBuyItem = function(_scope, item) {
        Store['buyTerrain']({
          terrainId: item.id,
          size: $scope.size,
          currentState: "/buy_base"
        }, null, function(result) {
          if (result.balans) {
            $scope.$root.farmer = result;
            $scope.$root.$emit('shop-hide');
          } else {
            alert("NEMA PARI!");
          }
        }).$promise['finally'](function() {
          $scope.$root.$emit('item-bought');
        });
      };

      $scope.unreg = $scope.$root.$on('buy-item', $scope.onSelectSize);

      $scope.$on("$destroy", function() {
        $scope.unreg();
      })

    }]);

Game.controller('BuySeadlingsController', ['$scope', '$translate', '$http',
    'Store', 'StoreItems',
    function($scope, $translate, $http, Store, StoreItems) {

      $scope.items = StoreItems['apple-type'];

      $scope.$root.$emit('shop-show', {
        items: $scope.items,
        showNext: false,
        storeUrl: '/public/images/game/jabolko.png'
      });

      $scope.onSelectSize = function(_scope, item) {
        $scope.plantTypeId = item.id;
        $scope.unreg();
        $scope.$root.$emit('shop-hide');
        $scope.$root.$emit('item-bought');
        $scope.items = StoreItems['seedling-type'];
        $scope.$root.$emit('shop-show', {
          items: $scope.items,
          showNext: false,
          storeUrl: '/public/images/game/grance.png'
        });
        $scope.unreg = $scope.$root.$on('buy-item', $scope.onBuyItem);

      };

      $scope.onBuyItem = function(_scope, item) {
        Store['buySeedling']({
          seedlingTypeId: item.id,
          plantTypeId: $scope.plantTypeId,
          currentState: "/plantation"
        }, null, function(result) {
          if (result.balans) {
            $scope.$root.farmer = result;
            $scope.$root.$emit('shop-hide');
          } else {
            alert("NEMA PARI!");
          }
        }).$promise['finally'](function() {
          $scope.$root.$emit('item-bought');
        });
      };

      $scope.unreg = $scope.$root.$on('buy-item', $scope.onSelectSize);

      $scope.$on("$destroy", function() {
        $scope.unreg();
      })

    }]);

Game.controller('PlantingController', [
    '$scope',
    '$translate',
    '$http',
    'StoreItems',
    function($scope, $translate, $http, StoreItems) {
      $http.post("/WeatherController/weatherforecast?fordays=5").success(
              function(data) {
                $scope.weather = data;
              });

      $scope.itemClick = function(a) {
        a.action();
      }

      $scope.items = StoreItems['traktor'];

      $scope.actions = [{
        ico: "prodavnica_home_icon",
        order: 1,
        action: function() {
          $scope.$root.$emit('shop-show', {
            items: $scope.items,
            showNext: true,
            storeUrl: '/public/images/game/grance.png'
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
          // $location.path("/buy_tractor");
          $location.path(data.currentState);
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
