![fiiiiii](https://github.com/user-attachments/assets/ceb6cde5-2fcc-4411-bb19-5518bed7aff2)

Spigot: https://www.spigotmc.org/resources/private-warps.121177/

Private Warps is a plugin for Minecraft that allows players to create, manage, and delete private warps on their server. With this plugin, players can have personal warps that only they can access, allowing them to teleport to specific places without other players having access. Additionally, administrators can configure a limit on the number of warps a player can create directly from the config.yml file.

The plugin is fully compatible with LuckPerms, meaning you can efficiently manage command permissions using this permission system. This allows administrators to customize which players or groups have access to the commands of Private Warps.
New Features:

    Message Customization: You can now customize the plugin’s messages as you wish. This includes the ability to change the plugin’s language, making it ideal for servers in different regions.
    Configurable Warp Limit per Player: The administrator can set a limit on the number of warps each player can create. This configuration is found in the config.yml file.
    Dynamic Configuration Reload: If you want to apply changes to the configuration, such as messages or warp limits, you can do so in real-time using the /pwarpreload command. This reloads the plugin's configuration without needing to restart the server.

Important Warning:

Private warps are saved in the pwarps.yml file. You should not edit this file while the server is running, as you may lose warps of players who are adding or modifying warps while you’re editing the file. To avoid data loss, it is recommended to make any changes to the pwarps.yml file only when the server is stopped or during a pause.
Key Features:

    Create private warps that only the owner player can use.
    Easy and quick teleportation to these warps.
    Configurable warp limit per player to manage resource usage from the config.yml file.
    Compatibility with LuckPerms for advanced permission management.

Commands:

    /setpwarp <name>
    Description: Creates a new private warp at the player’s current location. If a warp with the same name already exists, it will be overwritten.
    Usage: /setpwarp <name>
    Permission required: privatewarps.setpwarp

    /pwarp <name>
    Description: Teleports the player to a previously created private warp. In addition to teleporting, this command also shows a list of all private warps of the player.
    Usage: /pwarp <name>
    Permission required: privatewarps.pwarp

    /delpwarp <name>
    Description: Deletes a previously created private warp.
    Usage: /delpwarp <name>
    Permission required: privatewarps.delpwarp

    /pwarpreload
    Description: Reloads the plugin configuration, including messages and warp limits, without needing to restart the server.
    Usage: /pwarpreload
    Permission required: privatewarps.reload

Private Warps es un plugin para Minecraft que permite a los jugadores crear, gestionar y eliminar warps privados en su servidor. Con este plugin, los jugadores pueden tener warps personales a los que solo ellos pueden acceder, lo que les permite teletransportarse a lugares específicos sin que otros jugadores tengan acceso. Además, los administradores pueden configurar un límite en la cantidad de warps que un jugador puede crear directamente desde el archivo config.yml.

El plugin es completamente compatible con LuckPerms, lo que significa que puedes gestionar los permisos de los comandos de manera eficiente utilizando este sistema de permisos. Esto permite a los administradores personalizar qué jugadores o grupos tienen acceso a los comandos de Private Warps.
Novedades:

    Personalización de mensajes: Ahora es posible configurar los mensajes del plugin como desees. Esto incluye la capacidad de cambiar el idioma del plugin, lo que lo hace ideal para servidores de diferentes idiomas.
    Límite configurable de warps por jugador: El administrador puede establecer un límite en la cantidad de warps que cada jugador puede crear. Esta configuración se encuentra en el archivo config.yml.
    Recarga de configuración dinámica: Si deseas aplicar cambios en la configuración, como los mensajes o el límite de warps, puedes hacerlo en tiempo real usando el comando /pwarpreload. Esto recarga la configuración del plugin sin necesidad de reiniciar el servidor.

Advertencia importante:

Los warps privados se guardan en el archivo pwarps.yml. No se debe editar este archivo mientras el servidor esté en funcionamiento, ya que podrías perder los warps de jugadores que estén añadiendo o modificando warps mientras estás editando el archivo. Para evitar pérdidas de datos, se recomienda realizar cualquier cambio en el archivo pwarps.yml solo cuando el servidor esté detenido o durante una pausa.
Entre sus características más destacadas se incluyen:

    Creación de warps privados que solo el jugador propietario puede usar.
    Teletransportación fácil y rápida a estos warps.
    Límite configurable de warps por jugador para administrar el uso de recursos desde el archivo config.yml.
    Compatibilidad con LuckPerms para una gestión avanzada de permisos.

Comandos:

    /setpwarp <nombre>
    Descripción: Crea un nuevo warp privado en la ubicación actual del jugador. Si ya existe un warp con el mismo nombre, este será sobrescrito.
    Uso: /setpwarp <nombre>
    Permiso requerido: privatewarps.setpwarp

    /pwarp <nombre>
    Descripción: Teletransporta al jugador a un warp privado previamente creado. Además de realizar el teletransporte, este comando también muestra una lista de todos los warps privados del jugador.
    Uso: /pwarp <nombre>
    Permiso requerido: privatewarps.pwarp

    /delpwarp <nombre>
    Descripción: Elimina un warp privado previamente creado.
    Uso: /delpwarp <nombre>
    Permiso requerido: privatewarps.delpwarp

    /pwarpreload
    Descripción: Recarga la configuración del plugin, incluyendo los mensajes y el límite de warps, sin necesidad de reiniciar el servidor.
    Uso: /pwarpreload
    Permiso requerido: privatewarps.reload
