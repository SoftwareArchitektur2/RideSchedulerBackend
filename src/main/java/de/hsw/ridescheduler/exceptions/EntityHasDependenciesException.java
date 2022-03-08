package de.hsw.ridescheduler.exceptions;

public abstract class EntityHasDependenciesException extends RuntimeException {

    public EntityHasDependenciesException(String entityName, String dependencyName, String dependencies) {
        super(String.format("%s hat noch %s. Lösche zuerst die Abhängigkeiten.\nKonkret sind noch (%s) übrig.", entityName, dependencyName, dependencies));
    }
}
