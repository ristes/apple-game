Game.controller('PlantingStateController', [
    '$scope',
    'Operations',
    '$farmer',
    '$plantation',
    '$weather',
    '$window',
    'Planting',
    function($scope, Operations, $farmer, $plantation, $weather, $window,
            Planting) {

      $scope.availableSeedlings = 0;

      Planting.availableSeedlings(function(num) {
        $scope.availableSeedlings = num;
      });

      $scope.showShop = function() {
        $scope.$root.$emit('operation-store', {
          ico: "/public/images/game/operations/shop.png",
          name: 'store',
          price: 0,
          duration: 0,
          order: 1
        });
      };

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
          if (el.x === p.x && el.y === p.y) {  return true; }
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
      $scope.dif = 35;
      $scope.soilClick = function(p) {
        if ($scope.planting) {
          if (p.active && !p.tree) {
            if ($scope.availableSeedlings > 0) {
              // p.tree = "/public/images/game/plant.png";
              p.tree = "red";
              p.treeCls = 'seedling no-mouse-event';

              $scope.availableSeedlings -= $scope.dif;
              if ($scope.availableSeedlings < 0) {
                $scope.dif = 35 + $scope.availableSeedlings;
                $scope.availableSeedlings = 0;
              }
              $scope.coords.push({
                x: p.x,
                y: p.y
              });
            }
          } else if (p.tree) {
            delete p.tree;
            delete p.treeCls;
            $scope.availableSeedlings += $scope.dif;

            $scope.dif = 35;
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

      $scope.treeClick = function(t) {
        // does nothing
      };

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
            cls: 'pocva clickable'
          })

        }
        for (var i = 0; i < N; i++) {
          var p = r.cols[i];
          if (p.active && isInCoords(p)) {
            //r.cols[i].tree = $scope.$root.farmer.plant_url;
        	  r.cols[i].tree = p.plantType;
            r.cols[i].treeCls = 'seedling no-mouse-event';
          }
        }

      }

    }]);