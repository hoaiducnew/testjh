(function() {
    'use strict';

    angular
        .module('scmsApp')
        .controller('FanClubDialogController', FanClubDialogController);

    FanClubDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FanClub'];

    function FanClubDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FanClub) {
        var vm = this;

        vm.fanClub = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fanClub.id !== null) {
                FanClub.update(vm.fanClub, onSaveSuccess, onSaveError);
            } else {
                FanClub.save(vm.fanClub, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('scmsApp:fanClubUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
