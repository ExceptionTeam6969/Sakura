package dev.exceptionteam.sakura.utils.clazz

import java.io.File
import java.net.URL
import java.nio.file.Paths
import java.util.function.Predicate
import java.util.jar.JarInputStream

object ClassUtils {
    @Suppress("UNCHECKED_CAST")
    val <T> Class<out T>.instance
        get() = this.getDeclaredField("INSTANCE")[null] as T

    fun findSubclasses(
        packageName: String,
        parentClass: Class<*>,  // 目标父类
        includeInterfaces: Boolean = false // 是否包含接口实现类
    ): List<Class<*>> {
        return findClasses(packageName) { className ->
            try {
                val clazz = Class.forName(className, false, Thread.currentThread().contextClassLoader)
                if (includeInterfaces) {
                    parentClass.isAssignableFrom(clazz) && clazz != parentClass
                } else {
                    parentClass.isAssignableFrom(clazz) && !clazz.isInterface && clazz != parentClass
                }
            } catch (e: Throwable) {
                false
            }
        }
    }

    fun findClasses(
        packageName: String,
        predicate: Predicate<String> = Predicate { true }
    ): List<Class<*>> {
        val classLoader = Thread.currentThread().contextClassLoader
        val packagePath = packageName.replace('.', '/')

        val root = if (packageName.startsWith("dev.exceptionteam.sakura")) {
            val thisFileName = this.javaClass.name.replace('.', '/') + ".class"
            val thisURL = classLoader.getResource(thisFileName)!!
            val file = thisURL.file.substringBeforeLast(thisFileName.substringAfter("dev/exceptionteam/sakura"))
            URL(thisURL.protocol, thisURL.host, file)
        } else {
            classLoader.getResource(packagePath)!!
        }

        val isJar = root.toString().startsWith("jar")
        val classes = ArrayList<Class<*>>()

        if (isJar) {
            val path = Paths.get(URL(root.path.substringBeforeLast('!')).toURI())
            findClassesInJar(classLoader, path.toFile(), packagePath, predicate, classes)
        } else {
            classLoader.getResources(packagePath)?.let {
                for (url in it) {
                    findClasses(classLoader, File(url.file), packageName, predicate, classes)
                }
            }
        }

        return classes
    }

    private fun findClasses(
        classLoader: ClassLoader,
        directory: File,
        packageName: String,
        predicate: Predicate<String> = Predicate { true },
        list: MutableList<Class<*>>
    ): List<Class<*>> {
        if (!directory.exists()) return list

        val packagePath = packageName.replace('.', File.separatorChar)

        directory.walk()
            .filter { it.isFile }
            .filter { it.extension == "class" }
            .map { it.path.substringAfter(packagePath) }
            .map { it.replace(File.separatorChar, '.') }
            .map { it.substring(0, it.length - 6) }
            .map { "$packageName$it" }
            .filter { predicate.test(it) }
            .filter { !it.contains(".mixins.") }  // 跳过 Mixin 相关类
            .mapNotNull { className ->
                try {
                    val clazz = Class.forName(className, false, classLoader)

                    // 强制修改类的构造函数访问权限
                    clazz.declaredConstructors.forEach { it.isAccessible = true }

                    clazz
                } catch (e: Throwable) {
                    error("Failed to load class $className: ${e.message}")
                }
            }
            .toCollection(list)

        return list
    }

    private fun findClassesInJar(
        classLoader: ClassLoader,
        jarFile: File,
        packageName: String,
        predicate: Predicate<String> = Predicate { true },
        list: MutableList<Class<*>>
    ) {
        JarInputStream(jarFile.inputStream().buffered(1024 * 1024)).use {
            var entry = it.nextJarEntry
            while (entry != null) {
                if (!entry.isDirectory) {
                    val name = entry.name

                    if (name.startsWith(packageName) && name.endsWith(".class")) {
                        val className = name
                            .replace('/', '.')
                            .substring(0, name.length - 6)

                        if (predicate.test(className)) {
                            list.add(Class.forName(className, false, classLoader))
                        }
                    }
                }

                entry = it.nextJarEntry
            }
        }
    }
}