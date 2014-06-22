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
    'jQuery',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $plantation, $weather, $interval, $timeout, $irrigate, $) {

      $scope.visible = false;
      $scope.irrigationUrl = '/public/images/game/operations/irrigation.png';
      $scope.showNext = true;
      $scope.enableOther = true;

      $scope.holder = {};
      $scope.holder.duration = 0;

      function onIrrigation(_s, oper) {
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
          var types = extractIrrigationItems();

          if (types.length > 1) {

            // select which irrigation to start
            $scope.$root.$emit('shop-show', {
              items: types,
              showNext: true,
              storeUrl: oper.ico,
              shop: {
                name: 'irrigation'
              },
              onItemClick: showIrrigation
            });
          } else {
            showIrrigation(types[0]);
          }
        }
      }

      function showIrrigation(type) {
        $scope.$root.$emit('shop-hide');
        if ($scope.hasTensiometer) {
          var t = (type.name == 'KapkovoNavodnuvanje' ? 1 : 0);
          $scope.startIrrigate = $irrigate[t ? 'groovesIrritagion'
                  : 'dropsIrrigation'];
          var res = $irrigate.tensiometerTime(t ? 1 : 0);
          res.success(function(data) {
            $scope.holder.duration = data;
            $scope.holder.best = data;
          });
        }
        $scope.type = type;
        $scope.visible = true;
      }

      $scope.hide = function() {
        $scope.visible = false;
      }

      $scope.irrigate = function() {
        var interval = 100;
        var time = $scope.holder.duration * 50;
        $scope.visible = false;

        if ($scope.enableOther) {
          $scope.status = 0;
          $scope.enableOther = false;
          $interval(function() {
            $scope.status += 100 / time;
            $scope.showStatus = Math.round($scope.status);
            if ($scope.status > 100) {
              $scope.status = 100;
            }

          }, interval, time);

          $timeout(function() {
            $scope.enableOther = true;
            $scope.startIrrigate($scope.holder.duration);
          }, interval * (time + 1));

        }
      }

      function onBuyItem(item) {
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
      }
      ;

      var unreg = $scope.$root.$on('operation-irrigation', onIrrigation);

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

      function extractIrrigationItems() {
        var types = $items.get('irrigation');

        var result = [];

        for (var i = 0; i < types.length; i++) {
          if (types[i].name != 'tensiometer') {
            $scope.hasTensiometer = true;
          } else {
            result.push(types[i]);
          }
        }

        return result;

      }

      $scope.cfg = {
        range: "max",
        min: 0,
        max: 25,
        slide: function(event, ui) {
          $scope.$apply(function() {
            $scope.holder.duration = ui.value;
          });
        }
      };

    }]);
