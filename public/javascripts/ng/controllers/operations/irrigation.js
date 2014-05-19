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
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $plantation, $weather, $interval, $timeout) {

      $scope.visible = false;
      $scope.irrigationUrl = '/public/images/game/operations/irrigation.png';
      $scope.showNext = true;
      $scope.enableOther = true;

      $scope.hide = function() {
        $scope.visible = false;
      }

      $scope.itemClick = function(item) {
        if ($scope.enableOther) {
          $scope.status = 0;
          $scope.enableOther = false;
          $interval(function() {
            $scope.status += Math.round(100 / item.time);
          }, 1000, item.time);

          $timeout(function() {
            $scope.enableOther = true;
          }, 1000 * (item.time + 1));

        }
      }

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
        $scope.visible = true;
        $scope.$root.$emit("side-hide");
      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);