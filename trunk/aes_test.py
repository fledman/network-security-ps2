import Crypto.Cipher.AES as AES
import struct
import copy

def testKeyFromFile(plain,cipher,key,keyPrime):
    fp = open(plain,"rb")
    p = fp.read()
    fp.close()

    fc = open(cipher,"rb")
    c = fc.read()
    fc.close()

    fk = open(key,"rb")
    k = fk.read()
    fk.close()

    fkp = open(keyPrime,"rb")
    kp = fkp.read()
    fkp.close()

    aes1 = AES.new(k)
    aes2 = AES.new(kp)

    mid = aes1.encrypt(p)
    final = aes2.encrypt(mid)

    print pretty_print_byte_string(k)
    print pretty_print_byte_string(kp)
    print pretty_print_byte_string(p)
    print pretty_print_byte_string(mid)
    print pretty_print_byte_string(c)
    print pretty_print_byte_string(final)
    
    if final == c:
        print "keys worked"
    else:
        print "keys failed"

def testKey(plain,cipher,key,keyPrime):
    fp = open(plain,"rb")
    p = fp.read()
    fp.close()

    fc = open(cipher,"rb")
    c = fc.read()
    fc.close()

    aes1 = AES.new(ints_to_string(key))
    mid = aes1.encrypt(p)
    
    aes2 = AES.new(ints_to_string(keyPrime))
    final = aes2.encrypt(mid)

    print pretty_print_byte_array(key)
    print pretty_print_byte_array(keyPrime)
    print pretty_print_byte_string(p)
    print pretty_print_byte_string(c)
    print pretty_print_byte_string(final)
    if final == c:
        print "keys worked"
    else:
        print "keys failed"

# These are miscellaneous string / integer conversion utilities. 
# (bit manipulation requires that each byte be represented as a Python
# int, but the input to the AES routine is a string)
def char_to_int(char):
    return struct.unpack("B", char)[0]

def int_to_char(x):
    return struct.pack("B", x)

def string_to_ints(string):
    return [char_to_int(x) for x in string]

def ints_to_string(ints):
    return "".join((int_to_char(x) for x in ints))

def pretty_print_byte_string(string):
    '''
    Returns a nicely-formatted string of the hex values of each
    character in the input string.
    '''
    return pretty_print_byte_array(string_to_ints(string))

def pretty_print_byte_array(buf):
    '''
    Returns a nicely-formatted string of the hex values of each
    byte in the input list.
    '''
    return " ".join(("%02x" % x for x in buf))
