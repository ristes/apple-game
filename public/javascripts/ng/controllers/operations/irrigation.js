Game.controller('IrrigationController', [
    '$scope',
    '$translate',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$plantation',
    '$weather',
    '$interval',
    '$timeout',
    '$irrigate',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $plantation, $weather, $interval, $timeout, $irrigate) {

      $scope.visible = false;
      $scope.irrigationUrl = '/public/images/game/operations/irrigation.png';
      $scope.showNext = true;
      $scope.enableOther = true;

      $scope.holder = {};
      $scope.holder.duration = 1;
      $scope.max = 12;

      $scope.showTensiometer = true;

      $scope.hide = function() {
        $scope.visible = false;
      }

      $scope.itemClick = function() {
        var interval = 200;
        var time = $scope.holder.duration * 5;

        if ($scope.enableOther) {
          $scope.status = 0;
          $scope.enableOther = false;
          $interval(function() {
            $scope.status += Math.round(100 / time);
            if ($scope.status > 100) {
              $scope.status = 100;
            }

          }, interval, time);

          $timeout(function() {
            $scope.enableOther = true;
            $irrigate.irrigate("BrazdiNavodnuvanje", time);
          }, interval * (time + 1));

        }
      }

      var onBuyItem = function(item) {
        Store['buyItem']({
          itemName: item.name,
          quantity: item.perHa ? $scope.plantation.area : 1,
          currentState: $scope.$root.farmer.currentState
        }, null, function(result) {
          if (result.balans) {
            $items.add(item.store, item);
            $farmer.swap(result);
            $scope.$root.$emit('shop-hide');
            $scope.visible = true;
          } else {
            $scope.$root.$emit('insuficient-funds');
          }
        }).$promise['finally'](function() {
          $scope.$root.$emit('item-bought');

        });
      };

      $scope.items = [{
        name: '1hour',
        time: 1,
        url: '/public/images/game/items/grooves.png'
      }, {
        name: '5hour',
        time: 5,
        url: '/public/images/game/items/grooves.png'
      }, {
        name: '10hour',
        time: 10,
        url: '/public/images/game/items/grooves.png'
      }, {
        name: '12hour',
        time: 12,
        url: '/public/images/game/items/grooves.png'
      }];

      var unreg = $scope.$root.$on('operation-irrigation', function(_s, oper) {
        $scope.$root.$emit("side-hide");
        var hasItem = $items.check('irrigation');
        if (!hasItem) {
          $scope.$root.$emit('shop-show', {
            items: StoreItems['irrigation'],
            showNext: true,
            storeUrl: oper.ico,
            shop: {
              name: 'irrigation'
            },
            onItemClick: onBuyItem
          });

        } else {
          var types = $items.get('irrigation');
          if (types.length > 1) {
            $scope.$root.$emit('shop-show', {
              items: types,
              showNext: true,
              storeUrl: oper.ico,
              shop: {
                name: 'irrigation'
              },
              onItemClick: function(t) {
                $scope.type = t;
                $scope.$root.$emit('shop-hide');
                $scope.visible = true;
              }
            });
          } else {
            $scope.type = types[0];
            $scope.visible = true;
          }
        }
      });

      var unregHide = $scope.$root.$on('shop-hide', function() {
        $scope.visible = false;
      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
        if (unregHide) {
          unregHide();
        }
      });

    }]);
