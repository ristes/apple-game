Game.controller('DeepPlowingController', [
    '$scope',
    'Plowing',
    function($scope, Plowing) {

      var unreg = $scope.$root.$on('operation-deep-plowing',
              function(_s, oper) {

                $scope.$root.$emit('show-progress-global', {
                  title: 'progress_deep_plowing',
                  duration: 10
                });
                Plowing.deepPlowing();
              });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);