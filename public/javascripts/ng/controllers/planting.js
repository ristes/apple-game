Game.controller('PlantingStateController', [
    '$scope',
    '$location',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$plantation',
    '$weather',
    'toaster',
    '$window',
    '$day',
    function($scope, $location, $http, Store, StoreItems, Operations, $farmer,
            $items, $plantation, $weather, toaster, $window, $day) {

      $scope.showShop = function() {
        $scope.$root.$emit('operation-store', {
          ico: "/public/images/game/operations/shop.png",
          name: 'store',
          price: 0,
          duration: 0,
          order: 1
        });
      }

      $scope.onResizeFunction = function() {
        $scope.atom = $window.innerWidth * 0.8 / 20;
      };

      // Call to the function when the page is first loaded
      $scope.onResizeFunction();

      angular.element($window).bind('resize', function() {
        $scope.onResizeFunction();
        $scope.$apply();
      });
      $weather.load();
      $farmer.load();
      $scope.actions = Operations['planting'];

      if (!$scope.$root.plantation) {
        $plantation.load();
        $scope.coords = [];
      } else {
        $scope.coords = JSON.parse($scope.$root.plantation.treePositions);
      }
      $scope.seedling = $scope.coords.length;
      $scope.totalSeedling = 40;

      var isInCoords = function(p) {
        var arr = $scope.coords;

        for (var i = 0; i < arr.length; i++) {
          var el = arr[i];
          if (el.x === p.x && el.y === p.y) { return true; }
        }
        return false;
      }

      $scope.plantingDone = function() {

        function successPlanting(data) {
          if (data.status == true) {
            $farmer.load();
          }
        }

        var data = $plantation.save(JSON.stringify($scope.coords),
                $scope.seedling, successPlanting);
        $scope.planting = false;

      };

      $scope.planting = true;

      $scope.portionClick = function(p) {
        if ($scope.planting) {
          if (p.active && !p.tree) {
            // p.tree = "/public/images/game/plant.png";
            p.tree = $scope.$root.farmer.plant_url;
            p.treeCls = 'seedling';
            $scope.seedling++;
            $scope.coords.push({
              x: p.x,
              y: p.y
            });
          } else if (p.tree) {
            delete p.tree;
            delete p.treeCls;
            $scope.seedling--;
            var oldCoords = $scope.coords;
            $scope.coords = [];
            for (var i = 0; i < oldCoords.length; i++) {
              var el = oldCoords[i];
              if (el.x == p.x && el.y == p.y) {

              } else {
                $scope.coords.push(el);
              }
            }
          }
        }

      }

      $scope.rows = [];
      // var N = 15, M = 5;
      var N = 15, M = 5;
      var r = {
        cols: []
      };
      $scope.rows.push(r);
      for (var i = 0; i < N; i++) {
        r.cols.push({
          show: true,
          staticV: true,
          x: i,
          y: j,
          terrain: '/public/images/ograda.png',
          cls: 'ograda-gore'
        })
      }

      for (var j = 0; j < M; j++) {
        var r = {
          cols: []
        };
        $scope.rows.push(r);
        for (var i = 0; i < N; i++) {
          r.cols.push({
            show: true,
            active: true,
            staticV: false,
            x: i,
            y: j,
            terrain: $scope.$root.farmer.soil_url,
            cls: 'pocva'
          })

        }
        for (var i = 0; i < N; i++) {
          var p = r.cols[i];
          if (p.active && isInCoords(p)) {
            r.cols[i].tree = $scope.$root.farmer.plant_url;
            r.cols[i].treeCls = 'seedling';
          }
        }

      }

    }]);