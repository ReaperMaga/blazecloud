import '../public/css/globals.css'
import type { Metadata } from 'next'
import { Roboto } from 'next/font/google'
import { ReactElement, ReactNode } from 'react'

const roboto = Roboto({ subsets: ['latin'], weight: '400' })

export const metadata: Metadata = {
  title: 'NextJs Template',
  description: 'Official Averix NextJS Template',
}

interface LayoutProps {
  children: ReactNode
}

export default function RootLayout({ children }: LayoutProps): ReactElement {
  return (
    <html lang="en">
      <body className={roboto.className}>{children}</body>
    </html>
  )
}
