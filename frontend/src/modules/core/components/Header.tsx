import { Box, Flex, Heading, Image, Input, InputGroup } from "@chakra-ui/react";
import { NavButton } from "./NavButton";
import { IconSearch } from "@tabler/icons-react";

export default function Header() {
  return (
    <Flex
      as="header"
      justify="space-between"
      align="center"
      px={6}
      py={4}
      bg="principal.500"
      color="white"
      shadow="card"
      maxH="80px"
      overflow="hidden"
    >
      <Logo />

      <NavButton to="/">Inicio</NavButton>

      <Flex align="center" gap={4}>
        <SearchBar />

        <NavButton to="/">Iniciar sesión</NavButton>
      </Flex>
    </Flex>
  );
}

function Logo() {
  return (
    <Flex align="center" gap={4}>
      <Box
        w="120px"
        minW="120px"
        h="120px"
        borderRadius="full"
        bg="white"
        display="flex"
        alignItems="center"
        justifyContent="center"
        boxShadow="md"
        overflow="hidden"
        hideBelow="md"
      >
        <Image
          src="/logo_blanco.png"
          alt="Logo cultura"
          w="100%"
          h="100%"
          objectFit="cover"
          p={2}
        />
      </Box>
      <Heading fontSize="xl">Cultura ETSII</Heading>
    </Flex>
  );
}

function SearchBar() {
  return (
    <InputGroup
      endElement={
        <Box color="gray.400">
          <IconSearch size={18} />
        </Box>
      }
      maxW="300px"
    >
      <Input
        placeholder="Buscar..."
        bg="background"
        borderRadius="full"
        px="20px"
        h="40px"
        border="1px solid"
        borderColor="gray.200"
        transition="all 0.2s"
        color="gray.700"
        _hover={{
          borderColor: "gray.300",
        }}
        _focus={{
          outline: "none",
          borderColor: "principal.500",
          boxShadow: "0 0 0 3px rgba(75,117,157,0.15)",
        }}
      />
    </InputGroup>
  );
}
