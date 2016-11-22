/** Created by a.kiselev on 16/11/2016. */

var ListDwellersViewModel = function (persons, abnormalConsumers) {
    _.forEach(persons, function(person) {
        person.abnormalConsumptions = [];
        var consumptions = _.filter(abnormalConsumers, function(consumer) {
            return consumer.personId === person.personId
        });
        _.forEach(consumptions, function(consumption){
            person.abnormalConsumptions.push(consumption);
        });
    });
    self.persons = ko.observableArray(persons);
};