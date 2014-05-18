'use strict';

Game.controller('LoginController', ['$scope', '$translate',
    function($scope, $translate) {

    }]);

Game.controller('StoreController', ['$scope', '$translate', '$http', 'Store',
    'StoreItems', function($scope, $translate, $http, Store, StoreItems) {

      $scope.initStore = function(store, nextState, servMethod, shopIcon) {
        $scope.nextState = nextState;
        $scope.servMethod = servMethod || 'buy';
        $scope.store = store;

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
            $scope.$root.items[$scope.store] = true;
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
        storeUrl: '/public/images/game/sadnici-icon.png'
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
          storeUrl: '/public/images/game/sadnici-icon.png'
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
            $scope.$root.items['seedlings'] = true;
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
    'Operations',
    function($scope, $translate, $http, StoreItems, Operations) {
      $http.post("/WeatherController/weatherforecast?fordays=5").success(
              function(data) {
                $scope.weather = data;
              });

      $scope.items = StoreItems['traktor'];

      $scope.actions = Operations['planting'];

      var listeners = [];
      for (var i = 0; i < $scope.actions.length; i++) {
        var a = $scope.actions[i]
        var unreg = $scope.$root.$on('operation-' + a.name, function(_scope,
                oper) {
          console.log(oper);
          if (oper.duration > 0) {
            $scope.$root.$emit('show-progress-global', {
              title: 'progress.' + oper.name,
              duration: oper.duration
            })
          }
        });
        listeners.push(unreg);
      }
      $scope.$on("$destroy", function() {
        for (var i = 0; i < listeners.length; i++) {
          listeners[i]();
        }
      });

//      var atom = 128;
//
//      var z = 0;
//      var rows = 30;
//      var fistRow = 6;
//      var columns = 25;
//
//      Crafty.init(1366, 768, 'plantation');
//
//      var iso = Crafty.isometric.size(atom);
//      var field = new Land(columns, rows, fistRow, iso, atom);
//      field.init();
//
//      Crafty.viewport.y = -32;
//      Crafty.viewport.x = -64;

    }]);

Game.controller('UserInfoController', ['$scope', '$translate', '$http',
    '$location', function($scope, $translate, $http, $location) {

      $.post("/AuthController/farmer", function(data) {
        $scope.$root.farmer = data;
        $scope.$root.items = {};
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
