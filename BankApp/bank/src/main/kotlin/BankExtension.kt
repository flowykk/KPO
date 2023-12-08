import kotlin.system.exitProcess

fun Bank() {
    while (true) {
        print("Выберите дальнейшие действия:\n" +
                "1. Посмотреть открытые счета.\n" +
                "2. Открыть новый счёт.\n" +
                "3. Пополнить счёт.\n" +
                "4. Перевести деньги между счетами.\n" +
                "0. Выход.\n" +
                "В качестве ответа введите одну цифру: ")

        var number = scanner.nextInt()
        while (number !in 0..5) {
            print("Ваш ответ должен находиться в диапазоне от 0 до 5.\nПопробуйте ещё раз: ")
            number = scanner.nextInt()
        }

        when(number) {
            0 -> exit()
            1 -> viewAccounts()
            2 -> createAccount()
            3 -> updateBalance()
            4 -> Transfer()
        }
    }
}

fun exit() {
    println("До свидания!")
    exitProcess(0)
}

fun viewAccounts() {
    if (accountList.size == 0) {
        println("У Вас нет ниодного открытого счёта!")
    }
    for (account in accountList) {
        println("${account.name} ${account.number}: ${account.balance}₽")
    }
}

fun createAccount() {
    print("Введите 6-ти значный номер Вашего счёта: ")
    var number: String? = readLine()
    while (!(number != null && !accountList.any {it.number == number} && number.all { it.isDigit() } && number.length == 6) ) {
        print("Номер счёта должен состоять только из цифр и иметь длину 6, или же Вы ввели неуникальный номер счёта!\nПопробуйте ещё раз: ")
        number = readLine()
    }

    print("Введите имя Вашего счёта: ")
    var name: String? = readLine()
    while (!(name != null && !accountList.any {it.name == name!!.uppercase()} && name.all { it.isLetter() } )) {
        print("Имя счёта должно состоять только из букв! Или же Вы ввели неуникальное имя счёта!\nПопробуйте ещё раз: ")
        name = readLine()
    }
    name = name.uppercase()

    accountList.add(Account(number, name, 0.0))
    println("Новый счёт $name $number успешно создан!")
}

fun updateBalance() {
    print("Введите 6-ти значный номер Вашего счёта: ")
    var number: String? = readLine()
    while (!(number != null && number.all { it.isDigit() } && number.length == 6) ) {
        print("Номер счёта должен состоять только из цифр и иметь длину 6!\nПопробуйте ещё раз: ")
        number = readLine()
    }

    if (!accountList.any {it.number == number} ) {
        println("Счёта с таким номером не существует! ")
        return
    }

    print("Введите сумму, которую хотите внести на баланс счёта $number: ")
    var update = scanner.nextDouble()
    while (update <= 0) {
        println("Вы не можете пополнять счёт на сумму, которая <= 0!\nПопробуйте ещё раз: ")
        update = scanner.nextDouble()
    }

    for (account in accountList) {
        if (account.number == number) {
            account.balance += update
            println("Пополнение произошло успешно!\n${account.name} ${account.number}: ${account.balance}₽")
            break
        }
    }
}

fun Transfer() {
    print("Введите 6-ти значный номер счёта, c которого нужно снять деньги: ")
    var number1: String? = readLine()
    while (!(number1 != null && number1.all { it.isDigit() } && number1.length == 6) ) {
        print("Номер счёта должен состоять только из цифр и иметь длину 6!\nПопробуйте ещё раз: ")
        number1 = readLine()
    }
    if (!accountList.any {it.number == number1} ) {
        println("Счёта с таким номером не существует! ")
        return
    }

    print("Введите 6-ти значный номер счёта, куда нужно отправить деньги: ")
    var number2: String? = readLine()
    while (!(number2 != null && number2.all { it.isDigit() } && number2.length == 6) ) {
        print("Номер счёта должен состоять только из цифр и иметь длину 6!\nПопробуйте ещё раз: ")
        number2 = readLine()
    }

    if (number1 == number2) {
        println("Здесь нельзя использовать одинаковые счета!")
        return
    }

    if (!accountList.any {it.number == number2} ) {
        println("Счёта с таким номером не существует! ")
        return
    }

    print("Введите сумму, которую хотите снять со счёта $number1 отправить на счёт $number2: ")
    var transfer = scanner.nextDouble()
    while (transfer <= 0) {
        println("Вы не можете пополнять счёт на сумму, которая <= 0!\nПопробуйте ещё раз: ")
        transfer = scanner.nextDouble()
    }

    for (account in accountList) {
        if (account.number == number1) {
            if (account.balance < transfer) {
                println("На счёте $number1 недостаточно средств!")
                return
            }
            account.balance -= transfer
            println("${account.name} ${account.number}: ${account.balance}₽")
        }
    }

    for (account in accountList) {
        if (account.number == number2) {
            account.balance += transfer
            println("${account.name} ${account.number}: ${account.balance}₽")
        }
    }

    println("Перевод средств в размере $transfer₽ со счёта $number1 на счёт $number2 произошёл успешно!")
}