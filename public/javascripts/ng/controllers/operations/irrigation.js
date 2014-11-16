Game.controller('IrrigationController', ['$scope', '$day', '$interval',
  '$timeout', 'Irrigate',
  function($scope, $day, $interval, $timeout, Irrigate) {

    $scope.visible = false;
    $scope.showNext = true;
    $scope.enableOther = true;
    $scope.info = {
      mmRain: $day.get().rain_values
    };

    $scope.holder = {};
    $scope.holder.duration = 0;


    function onIrrigation(_s, oper) {
      $scope.$root.$emit("side-hide");
      Irrigate.getTypeAsync(showIrrigation);
    }

    function showIrrigation(type) {
      if (!type) {
        console.log('no type');
        return;
      }
      $scope.$root.$emit('shop-hide');
      $scope.nextType = Irrigate.nextIrrigationType(type);
      $scope.type = type;
      $scope.startIrrigate = function(time) {
        Irrigate[$scope.type.name + 'Irrigation'](time);
      };
      $scope.hasTensiometer = type.hasTensiometer;
      if (type.hasTensiometer) {
        var res = Irrigate.tensiometerTime();
        res.success(function(data) {
          $scope.holder.duration = data;
          $scope.holder.best = data;
        });
      }

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

    $scope.buy = function(nextType) {
      Irrigate.buy(nextType, showIrrigation)
    }

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

  }
]);
