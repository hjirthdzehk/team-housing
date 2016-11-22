var AllDebtsViewModel = function(debts) {
    var debtsByDwellers =
        _.chain(debts)
            .groupBy(function(debt) {
                return debt.personId;
            })
            .map(function(debtsByDweller, index) {
                var debtsByFlat = _.chain(debtsByDweller)
                    .groupBy(function(debt) {
                        return debt.flatNumber;
                    })
                    .map(function(debtsByFlat, ind){
                        return {flat: debtsByFlat[0].flatNumber, debts: debtsByFlat};
                    })
                    .value();
                return {person: debtsByDweller[0].personName+debtsByDweller[0].personSurname, debtsByFlats: debtsByFlat};
            })
            .value();
    this.debtsByDwellers = ko.observableArray(debtsByDwellers);
};