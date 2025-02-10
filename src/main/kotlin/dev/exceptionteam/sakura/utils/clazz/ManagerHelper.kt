package dev.exceptionteam.sakura.utils.clazz

val classes = ClassUtils.findClasses("dev.exceptionteam.sakura") {
        !it.contains("asm")
}