Game.directive('spin', function() {
    return {
        templateUrl: '/public/templates/touch-spin.html',
        scope: {
          'value' : "=",
          'step':'=',
          'callback':'='
        },
        restrict: 'AE',
        link: function(scope, element, attrs, ngModel) {
          var min,max,step,value,input,initial;
 
          element = angular.element(element);
 
          if(typeof attrs === 'undefined'){
            throw new Error('Spin.js attributes missing');
          } else {
            min = typeof attrs.min !== 'undefined' ? attrs.min : 0;
            max = typeof attrs.max !== 'undefined' ? attrs.max : 999;
            step = parseInt(scope.step);
            callback = scope.callback;
            initial = parseInt(scope.value);
 
            input = $("input[name='spin']",element);
            input.TouchSpin({
                min: min,
                max: max,
                step: step,
                initval: initial,
                forcestepdivisibility : 'none',
                booster : false
            });
            scope.$watch('value', function(n,o) {
            	callback(scope.value);;
            })
            input.on('change', function(e){
              scope.value = input.val();
              
              //hack
              if(!scope.$$phase) {
                scope.$apply();
              }
            });
           
 
          }
        }
    };
}); 