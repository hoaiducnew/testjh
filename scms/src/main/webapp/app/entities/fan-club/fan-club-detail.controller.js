(function() {
    'use strict';

    angular
        .module('scmsApp')
        .controller('FanClubDetailController', FanClubDetailController);

    FanClubDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FanClub'];

    function FanClubDetailController($scope, $rootScope, $stateParams, previousState, entity, FanClub) {
        var vm = this;

        vm.fanClub = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('scmsApp:fanClubUpdate', function(event, result) {
            vm.fanClub = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
