Использованные паттерны:

* Builder (в ``Command Builder`` для построения цепочки комманд)
* Command (для реализации всех команд)
* Singleton (``Database, Parser, CallCommandFactory, TokenCommandFactory`` технически являются синглтонами)
* Factory (``CallCommandFactory, TokenCommandFactory`` для создания команд)