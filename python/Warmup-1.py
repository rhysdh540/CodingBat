def sleep_in(weekday, vacation):
    """
    The parameter weekday is True if it is a weekday, and the parameter vacation is True if we are on vacation.
    We sleep in if it is not a weekday or we're on vacation. Return True if we sleep in.
    """
    return not weekday or vacation


def monkey_trouble(a, b):
    """
    We have two monkeys, a and b, and the parameters a_smile and b_smile indicate if each is smiling.
    We are in trouble if they are both smiling or if neither of them is smiling. Return True if we are in trouble.
    """
    return a == b


def sum_double(a, b):
    """
    Given two int values, return their sum. Unless the two values are the same, then return double their sum.
    """
    return 4 * a if a == b else a + b


def diff21(n):
    """
    Given an int n, return the absolute difference between n and 21, except return double the absolute difference
    if n is over 21.
    """
    return 2 * (n - 21) if n > 21 else 21 - n


def parrot_trouble(talking, hour):
    """
    We have a loud talking parrot. The "hour" parameter is the current hour time in the range 0..23.
    We are in trouble if the parrot is talking and the hour is before 7 or after 20. Return True if we are in trouble.
    """
    return talking and (hour > 20 or hour < 7)


def makes10(a, b):
    """
    Given 2 ints, a and b, return True if one if them is 10 or if their sum is 10.
    """
    return a == 10 or b == 10 or a + b == 10


def near_hundred(n):
    """
    Given an int n, return True if it is within 10 of 100 or 200.
    """
    return abs(100 - n) <= 10 or abs(200 - n) <= 10


def pos_neg(a, b, negative):
    """
    Given 2 int values, return True if one is negative and one is positive.
    Except if the parameter "negative" is True, then return True only if both are negative.
    """
    return (a < 0 < b or a > 0 > b) if not negative else (a < 0 and b < 0)


def not_string(str):
    """
    Given a string, return a new string where "not " has been added to the front.
    However, if the string already begins with "not", return the string unchanged.
    """
    return str if str.startswith("not") else "not " + str


def missing_char(str, n):
    """
    Given a non-empty string and an int n, return a new string where the char at index n has been removed.
    The value of n will be a valid index of a char in the original string
    (i.e. n will be in the range 0..len(str)-1 inclusive).
    """
    return str[:n] + str[n + 1:]


def front_back(str):
    """
    Given a string, return a new string where the first and last chars have been exchanged.
    """
    return str[-1:] + str[1:-1] + str[:1] if len(str) > 1 else str


def front3(str):
    """
    Given a string, we'll say that the front is the first 3 chars of the string.
    If the string length is less than 3, the front is whatever is there.
    Return a new string which is 3 copies of the front.
    """
    return str[:3] * 3
