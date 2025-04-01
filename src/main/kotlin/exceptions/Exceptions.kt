package org.example.exceptions

sealed class Exceptions (message:String):Exception(message){
    class ValidationException(message:String):Exceptions("Error en la validación: $message")
    class NotFoundException(message:String):Exceptions("Vehículo no encontrado: $message")
    class StorageException(message:String):Exceptions("Error en el almacenamiento: $message")
}