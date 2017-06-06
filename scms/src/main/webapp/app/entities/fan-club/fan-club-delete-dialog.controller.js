(function() {
    'use strict';

    angular
        .module('scmsApp')
        .controller('FanClubDeleteController',FanClubDeleteController);

    FanClubDeleteController.$inject = ['$uibModalInstance', 'entity', 'FanClub'];

    function FanClubDeleteController($uibModalInstance, entity, FanClub) {
        var vm = this;

        vm.fanClub = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FanClub.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
